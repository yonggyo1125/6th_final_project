package org.sparta.monitoringserver.operation.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/operation")
public class OperationController {
    private final ObjectMapper objectMapper;

    @PostMapping(value = "/interactive", consumes = "application/x-www-form-urlencoded")
    public Mono<Void> handleInteractive(@RequestParam("payload") String payloadString) {

        return Mono.fromCallable(() -> objectMapper.readTree(payloadString))
                .flatMap(payload -> {
                    String actionId = payload.at("/actions/0/action_id").asText();
                    String serviceName = payload.at("/actions/0/value").asText();
                    String responseUrl = payload.path("response_url").asText();

                    log.info("슬랙 액션 수신: actionId={}, service={}", actionId, serviceName);

                    // 액션에 따른 조치 처리


                    return Mono.empty();
                });
    }
}

