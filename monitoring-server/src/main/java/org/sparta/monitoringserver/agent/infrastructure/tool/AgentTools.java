package org.sparta.monitoringserver.agent.infrastructure.tool;


import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class AgentTools {
    private final WebClient lokiWebClient;
    private final WebClient slackWebClient;
    private final ObjectMapper objectMapper;

    public AgentTools(
            @Value("${monitoring.loki.url}") String lokiUrl,
            @Value("${monitoring.slack.url}") String slackUrl,
            ObjectMapper objectMapper) {
        this.lokiWebClient = WebClient.builder().baseUrl(lokiUrl).build();
        this.slackWebClient = WebClient.builder().baseUrl(slackUrl).build();
        this.objectMapper = objectMapper;
    }

    // Loki 에러 로그 추출
    @Tool(description = "Loki에서 특정 애플리케이션의 'ERROR' 레벨 로그만 추출합니다.")
    public String fetchErrorLogs(
            @ToolParam(description = "로그를 조회할 대상 애플리케이션의 이름 (예: 'order-service', 'lecture-service')")
            String appName,
            @ToolParam(description = "조회할 로그의 최대 개수 (기본값: 50, 최대: 100)", required = false)
            Integer limit) {

        int finalLimit = (limit != null) ? limit : 50;
        String logQl = String.format("{app=\"%s\"} |= \"ERROR\"", appName);

        log.info("에러 로그 추출 수행: appName={}, limit={}", appName, finalLimit);

        return lokiWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/loki/api/v1/query_range")
                        .queryParam("query", logQl)
                        .queryParam("limit", finalLimit)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .onErrorReturn("Loki 조회 중 오류가 발생했습니다.")
                .block();
    }

    // Slack 알림
    @Tool(description = "장애 분석 결과와 즉각 대응을 위한 액션 버튼이 포함된 슬랙 리포트를 발송합니다.")
    public String sendDetailedSlackReport(
            @ToolParam(description = "위험도 (CRITICAL, WARNING)") String severity,
            @ToolParam(description = "장애 서비스 명") String service,
            @ToolParam(description = "분석 내용 (마크다운 포맷)") String analysis,
            @ToolParam(description = "추천 조치 사항") String recommendedAction) {

        log.info("상세 슬랙 리포트 구성 중: service={}", service);

        try {
            // Block Kit 구조 생성
            List<Object> blocks = new ArrayList<>();

            // 1. Header
            String emoji = "critical".equalsIgnoreCase(severity) ? "🚨" : "⚠️";
            blocks.add(createHeader(String.format("%s [%s] %s 서비스 이상 감지", emoji, severity.toUpperCase(), service)));

            // 2. Summary Section
            blocks.add(createSection(String.format("*분석 결과:*\n%s", analysis)));
            blocks.add(createSection(String.format("*추천 조치:*\n%s", recommendedAction)));

            // 3. Divider
            blocks.add(Map.of("type", "divider"));

            // 4. Action Buttons
            blocks.add(createActionButtons(service));

            // JSON 직렬화
            String payload = objectMapper.writeValueAsString(Map.of("blocks", blocks));

            return slackWebClient.post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

        } catch (Exception e) {
            log.error("슬랙 메시지 생성 실패", e);
            return "슬랙 발송 중 오류 발생";
        }
    }

    private Map<String, Object> createHeader(String text) {
        return Map.of(
                "type", "header",
                "text", Map.of("type", "plain_text", "text", text, "emoji", true)
        );
    }

    private Map<String, Object> createSection(String markdownText) {
        return Map.of(
                "type", "section",
                "text", Map.of("type", "mrkdwn", "text", markdownText)
        );
    }

    private Map<String, Object> createActionButtons(String service) {
        return Map.of(
                "type", "actions",
                "elements", List.of(
                        Map.of(
                                "type", "button",
                                "text", Map.of("type", "plain_text", "text", "로그 상세 보기"),
                                "style", "primary",
                                "action_id", "view_loki_logs",
                                "value", service
                        ),
                        Map.of(
                                "type", "button",
                                "text", Map.of("type", "plain_text", "text", "인스턴스 재시작"),
                                "style", "danger",
                                "action_id", "restart_service",
                                "value", service,
                                "confirm", Map.of(
                                        "title", Map.of("type", "plain_text", "text", "정말 재시작할까요?"),
                                        "text", Map.of("type", "mrkdwn", "text", "재시작 시 일시적인 서비스 단절이 발생할 수 있습니다."),
                                        "confirm", Map.of("type", "plain_text", "text", "실행"),
                                        "deny", Map.of("type", "plain_text", "text", "취소")
                                )
                        )
                )
        );
    }
}
