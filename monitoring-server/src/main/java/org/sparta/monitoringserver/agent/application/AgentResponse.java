package org.sparta.monitoringserver.agent.application;

import java.time.LocalDateTime;

public record AgentResponse(
        String content,
        String model,
        Integer usageTokens,
        long durationMs,
        LocalDateTime executedAt
) {}