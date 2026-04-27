package org.sparta.monitoringserver.agent.infrastructure.tool;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

        ConnectionProvider provider = ConnectionProvider.builder("monitoring-pool")
                .maxConnections(50)
                .maxIdleTime(Duration.ofSeconds(20))
                .maxLifeTime(Duration.ofSeconds(60))
                .pendingAcquireTimeout(Duration.ofSeconds(60))
                .evictInBackground(Duration.ofSeconds(120))
                .build();

        HttpClient httpClient = HttpClient.create(provider)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(30, TimeUnit.SECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(30, TimeUnit.SECONDS)))
                .keepAlive(true);

        ReactorClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);

        this.lokiWebClient = WebClient.builder()
                .baseUrl(lokiUrl)
                .clientConnector(connector)
                .build();

        this.slackWebClient = WebClient.builder()
                .baseUrl(slackUrl)
                .clientConnector(connector)
                .build();

        this.objectMapper = objectMapper;
    }

    @Tool(description = "Loki에서 특정 애플리케이션의 'ERROR' 레벨 로그를 추출합니다.", name = "errorLogs")
    public String fetchErrorLogs(
            @ToolParam(description = "대상 애플리케이션 명 (예: 'order-service')") String appName,
            @ToolParam(description = "로그 최대 개수 (기본 50)", required = false) Integer limit) {

        int finalLimit = (limit != null) ? limit : 50;
        String logQl = String.format("{app=\"%s\"} |= \"ERROR\"", appName);

        log.info("Loki 에러 로그 추출 시도: appName={}, limit={}", appName, finalLimit);

        try {
            return lokiWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/loki/api/v1/query_range")
                            .queryParam("query", "{query}")
                            .queryParam("limit", finalLimit)
                            .build(logQl))
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(10)) // 10초 타임아웃 설정
                    .onErrorResume(e -> {
                        log.error("Loki 조회 실패: {}", e.getMessage());
                        return Mono.just("현재 Loki 시스템에서 로그를 가져올 수 없습니다. 사유: " + e.getMessage());
                    })
                    .block(); // [핵심] LLM에게 결과값을 동기적으로 전달
        } catch (Exception e) {
            log.error("Loki 호출 중 예외 발생", e);
            return "Loki 통신 장애 발생";
        }
    }


    @Tool(description = "분석 결과와 액션 버튼이 포함된 슬랙 리포트를 발송합니다.")
    public String sendDetailedSlackReport(
            @ToolParam(description = "위험도 (CRITICAL, WARNING)") String severity,
            @ToolParam(description = "장애 서비스 명") String service,
            @ToolParam(description = "분석 내용 (마크다운)") String analysis,
            @ToolParam(description = "추천 조치 사항") String recommendedAction) {

        log.info("슬랙 리포트 발송 프로세스 시작: service={}", service);

        try {
            List<Object> blocks = new ArrayList<>();
            String emoji = "critical".equalsIgnoreCase(severity) ? "🚨" : "⚠️";

            blocks.add(Map.of("type", "header", "text", Map.of("type", "plain_text", "text", String.format("%s [%s] %s 서비스 이상 감지", emoji, severity.toUpperCase(), service), "emoji", true)));
            blocks.add(Map.of("type", "section", "text", Map.of("type", "mrkdwn", "text", String.format("*분석 결과:*\n%s", analysis))));
            blocks.add(Map.of("type", "section", "text", Map.of("type", "mrkdwn", "text", String.format("*추천 조치:*\n%s", recommendedAction))));
            blocks.add(Map.of("type", "divider"));
            blocks.add(createActionButtons(service));

            String payload = objectMapper.writeValueAsString(Map.of("blocks", blocks));
            log.info("슬랙 전달 payload: {}", payload);

            String result = slackWebClient.post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(5))
                    .block(); // [핵심] 전송 완료 후 응답이 올 때까지 대기

            log.info("슬랙 전송 성공 응답: {}", result);
            return "슬랙 보고 완료: " + result;

        } catch (Exception e) {
            log.error("슬랙 메시지 발송 실패", e);
            return "슬랙 메시지 전송 중 오류 발생: " + e.getMessage();
        }
    }

    private Map<String, Object> createActionButtons(String service) {
        return Map.of(
                "type", "actions",
                "elements", List.of(
                        Map.of("type", "button", "text", Map.of("type", "plain_text", "text", "로그 상세 보기"), "style", "primary", "action_id", "view_logs", "value", service),
                        Map.of("type", "button", "text", Map.of("type", "plain_text", "text", "서비스 재시작"), "style", "danger", "action_id", "restart", "value", service)
                )
        );
    }
}