package org.sparta.monitoringserver.agent.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.monitoringserver.agent.application.AgentResponse;
import org.sparta.monitoringserver.agent.application.AiAgent;
import org.sparta.monitoringserver.prompt.application.PromptService;
import org.sparta.monitoringserver.prompt.domain.Prompt;
import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.anthropic.AnthropicChatOptions;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.time.Duration;
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


    private static final String[] MONITORING_TOOLS = {"fetchErrorLogs", "sendDetailedSlackReport"};

    @Override
    public Flux<AgentResponse> assignTask(String promptName, Map<String, Object> inputs) {
        return promptService.getActiveLatestPrompt(promptName)
                .flatMapMany(prompt -> {
                    ChatModel model = selectModel(prompt.getModelName());

                    return executeTaskStream(model, prompt, inputs)
                            .retryWhen(Retry.backoff(2, Duration.ofSeconds(1))
                                    .filter(e -> !(e instanceof org.springframework.web.client.HttpClientErrorException))
                                    .doBeforeRetry(signal -> log.warn("LLM 재시도 중... 사유: {}", signal.failure().getMessage())))
                            .onErrorResume(e -> {
                                log.error("에이전트 실행 중 최종 오류 발생, Fallback 검토 필요: {}", e.getMessage());
                                return Flux.empty();
                            });
                })
                .doOnError(e -> log.error("모니터링 분석 스트리밍 중 치명적 오류: {}", e.getMessage(), e));
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


        Message systemMessage = new SystemMessage(prompt.getSystemPrompt());
        PromptTemplate userTemplate = new PromptTemplate(prompt.getContent());
        Message userMessage = new UserMessage(userTemplate.render(inputs));

        // 모델별 @Tool 연동을 포함한 옵션 생성
        ChatOptions options = buildOptions(prompt);

        log.info("에이전트 태스크 가동: 모델={}, 버전={}, 도구={}",
                prompt.getModelName(), prompt.getVersion(), MONITORING_TOOLS);

        // 스트리밍 호출 및 응답 매핑
        return model.stream(new org.springframework.ai.chat.prompt.Prompt(List.of(systemMessage, userMessage), options))
                .map(response -> {
                    // 토큰 사용량 정보 추출
                    if (response.getMetadata().getUsage() != null) {
                        totalTokens.set(response.getMetadata().getUsage().getTotalTokens());
                    }

                    // 응답 텍스트 추출 (Tool Call 시점의 빈 메시지 방어)
                    String content = (response.getResult() != null && response.getResult().getOutput() != null)
                            ? response.getResult().getOutput().getText()
                            : "";

                    return new AgentResponse(
                            content,
                            prompt.getModelName(),
                            prompt.getVersion(),
                            totalTokens.get(),
                            System.currentTimeMillis() - startTime,
                            LocalDateTime.now()
                    );
                })
                // 내용이 있거나 마지막 토큰 정보가 있는 경우만 필터링하여 노이즈 제거
                .filter(res -> StringUtils.hasText(res.content()) || res.usageTokens() > 0)
                .doOnComplete(() -> log.info("에이전트 분석 완료: 총 소요시간={}ms", System.currentTimeMillis() - startTime));
    }


    private ChatOptions buildOptions(Prompt prompt) {
        String modelName = prompt.getModelName().toLowerCase();
        if (modelName.contains("claude")) {
            return AnthropicChatOptions.builder()
                    .model(prompt.getModelName())
                    .temperature(prompt.getTemperature())
                    .maxTokens(prompt.getMaxTokens())
                    .toolNames(MONITORING_TOOLS)
                    .build();
        } else {
            return OpenAiChatOptions.builder()
                    .model(prompt.getModelName())
                    .temperature(prompt.getTemperature())
                    .maxTokens(prompt.getMaxTokens())
                    .toolNames(MONITORING_TOOLS)
                    .build();
        }
    }
}