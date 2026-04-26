package org.sparta.monitoringserver.agent.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.monitoringserver.agent.application.AgentResponse;
import org.sparta.monitoringserver.agent.application.AiAgent;
import org.sparta.monitoringserver.prompt.application.PromptService;
import org.sparta.monitoringserver.prompt.domain.Prompt;
import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@RequiredArgsConstructor
public class AiAgentImpl implements AiAgent {

    private final OpenAiChatModel openAiChatModel;
    private final AnthropicChatModel anthropicChatModel;
    private final PromptService promptService;

    @Override
    public Flux<AgentResponse> assignTask(String promptName, Map<String, Object> inputs) {
        return promptService.getActiveLatestPrompt(promptName)
                .flatMapMany(prompt -> {
                    ChatModel model = selectModel(prompt.getModelName());
                    return executeTaskStream(model, prompt, inputs);
                })
                .doOnError(e -> log.error("모니터링 분석 스트리밍 중 오류 발생: {}", e.getMessage(), e));
    }

    private ChatModel selectModel(String modelName) {
        if (modelName != null && modelName.toLowerCase().contains("claude")) {
            return anthropicChatModel;
        }
        return openAiChatModel;
    }


    private Flux<AgentResponse> executeTaskStream(ChatModel model, Prompt prompt, Map<String, Object> inputs) {
        long startTime = System.currentTimeMillis();

        AtomicInteger totalTokens = new AtomicInteger(0);

        // 메시지 및 옵션 구성
        Message systemMessage = new SystemMessage(prompt.getSystemPrompt());
        PromptTemplate userTemplate = new PromptTemplate(prompt.getContent());
        Message userMessage = new UserMessage(userTemplate.render(inputs));

        ChatOptions options = ChatOptions.builder()
                .model(prompt.getModelName())
                .temperature(prompt.getTemperature())
                .maxTokens(prompt.getMaxTokens())
                .build();

        // 스트리밍 호출
        return model.stream(new org.springframework.ai.chat.prompt.Prompt(List.of(systemMessage, userMessage), options))
                .map(response -> {
                    // 각 응답 조각(Chunk)에서 메타데이터 추출
                    if (response.getMetadata().getUsage() != null) {
                        totalTokens.set(response.getMetadata().getUsage().getTotalTokens());
                    }

                    String content = (response.getResult() != null && response.getResult().getOutput() != null)
                            ? response.getResult().getOutput().getText()
                            : "";

                    // 응답 객체 생성
                    return new AgentResponse(
                            content,
                            prompt.getModelName(),
                            prompt.getVersion(),
                            totalTokens.get(),
                            System.currentTimeMillis() - startTime,
                            LocalDateTime.now()
                    );
                })
                .filter(res -> StringUtils.hasText(res.content()) || res.usageTokens() > 0)
                .doOnComplete(() -> log.info("에이전트 스트리밍 완료: 모델={}, 총 소요시간={}ms",
                        prompt.getModelName(), System.currentTimeMillis() - startTime));
    }
}