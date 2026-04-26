package org.sparta.monitoringserver.agent.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.monitoringserver.agent.application.AgentResponse;
import org.sparta.monitoringserver.agent.application.AiAgent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AiAgentImpl implements AiAgent {
    @Override
    public Mono<AgentResponse> assignTask(String agentName, Map<String, Object> inputs) {
        return null;
    }
}
