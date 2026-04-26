package org.sparta.monitoringserver.agent.application;

import reactor.core.publisher.Flux;

import java.util.Map;

public interface AiAgent {
    Flux<AgentResponse> assignTask(String promptName, Map<String, Object> inputs);
}
