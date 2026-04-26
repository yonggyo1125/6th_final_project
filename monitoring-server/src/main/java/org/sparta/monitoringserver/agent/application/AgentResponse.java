package org.sparta.monitoringserver.agent.application;

import java.time.LocalDateTime;

public record AgentResponse(
        String content,
        String model,
        String version,
        Integer usageTokens,
        long durationMs,
        LocalDateTime executedAt
) {

    public static AgentResponse chunk(String content, String model, String version) {
        return new AgentResponse(content, model, version, 0, 0, LocalDateTime.now());
    }


    public static AgentResponse finalResponse(String content, String model, String version, Integer tokens, long duration) {
        return new AgentResponse(content, model, version, tokens, duration, LocalDateTime.now());
    }
}