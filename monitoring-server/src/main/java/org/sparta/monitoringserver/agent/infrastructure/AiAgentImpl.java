package org.sparta.monitoringserver.agent.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.sparta.monitoringserver.agent.application.AgentResponse;
import org.sparta.monitoringserver.agent.application.AiAgent;
import org.sparta.monitoringserver.agent.infrastructure.tool.AgentTools;
import org.sparta.monitoringserver.prompt.application.PromptService;
import org.sparta.monitoringserver.prompt.domain.Prompt;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class AiAgentImpl implements AiAgent {

    private final ChatClient chatClient;
    private final PromptService promptService;


    public AiAgentImpl(ChatClient.Builder chatClientBuilder,
                       PromptService promptService,
                       AgentTools agentTools) {
        this.promptService = promptService;
        this.chatClient = chatClientBuilder
                .defaultTools(agentTools)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    @Override
    public Flux<AgentResponse> assignTask(String promptName, Map<String, Object> inputs) {
        return promptService.getActiveLatestPrompt(promptName)
                .flatMapMany(prompt -> executeTaskStream(prompt, inputs));
    }

    private Flux<AgentResponse> executeTaskStream(Prompt prompt, Map<String, Object> inputs) {
        long startTime = System.currentTimeMillis();
        AtomicInteger totalTokens = new AtomicInteger(0);

        log.info("OpenAI 에이전트 가동: 모델={}, 버전={}", prompt.getModelName(), prompt.getVersion());

        return chatClient.prompt()
                .system(prompt.getSystemPrompt())
                .user(u -> u.text(prompt.getContent()).params(inputs))
                .options(ChatOptions.builder()
                        .model(prompt.getModelName())
                        .temperature(prompt.getTemperature())
                        .build())
                .stream()
                .chatResponse()
                .map(response -> {
                    // 토큰 사용량 정보 추출
                    if (response.getMetadata().getUsage() != null) {
                        totalTokens.set(response.getMetadata().getUsage().getTotalTokens());
                    }

                    String content = (response.getResult() != null)
                            ? response.getResult().getOutput().getText() : "";

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
                .doOnComplete(() -> log.info("분석 완료: 소요시간={}ms", System.currentTimeMillis() - startTime));
    }
}