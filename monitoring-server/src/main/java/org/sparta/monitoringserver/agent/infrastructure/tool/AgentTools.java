package org.sparta.monitoringserver.agent.infrastructure.tool;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
public class AgentTools {
    private final WebClient lokiWebClient;
    private final WebClient slackWebClient;

    public AgentTools(
            @Value("${monitoring.loki.url}") String lokiUrl,
            @Value("${monitoring.slack.url}") String slackUrl) {
        this.lokiWebClient = WebClient.builder().baseUrl(lokiUrl).build();
        this.slackWebClient = WebClient.builder().baseUrl(slackUrl).build();
    }
}
