package com.goggles.payment_service.infrastructure.toss.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goggles.payment_service.infrastructure.toss.topic.PaymentTopic;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(PaymentTopic.class)
public class TossClientFallbackFactory implements FallbackFactory<TossClient> {

    private final ObjectMapper objectMapper;

    @Override
    public TossClient create(Throwable e) {

        return new TossClient() {
            @Override
            public ResponseEntity<JsonNode> approve(Map<String, Object> params) {
                log.error("TOSS 결제 승인 요청 실패 - Params: {}, 사유: {}", params, e.getMessage(), e);

                return null;
            }

            @Override
            public ResponseEntity<JsonNode> cancel(String paymentKey, Map<String, String> params) {
                log.error("TOSS 취소 요청 실패 - PaymentKey: {}, Params: {}, 사유: {}", paymentKey, params, e.getMessage(), e);

                return null;
            }
        };
    }

    private ResponseEntity<JsonNode> getErrorResponse(String requestType, Throwable e) {
        if (e instanceof FeignException feignException) {
            int status = feignException.status();
            String body = feignException.contentUTF8();

            log.error("{} 실패 - 응답 상태코드: {}, 응답 Body:{}, 사유: {}", requestType, status, body, e.getMessage(), e);
        }

        log.error("{} 실패 - 사유: {}", requestType, e.getMessage(), e);
        return ResponseEntity.internalServerError().build();
    }
}
