package org.sparta.monitoringserver.agent.application;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface AiAgent {
    Mono<AgentResponse> assignTask(String agentName, Map<String, Object> inputs);
}
