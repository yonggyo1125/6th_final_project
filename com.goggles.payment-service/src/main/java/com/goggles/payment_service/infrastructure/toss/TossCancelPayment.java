package com.goggles.payment_service.infrastructure.toss;

import com.fasterxml.jackson.databind.JsonNode;
import com.goggles.payment_service.domain.PaymentId;
import com.goggles.payment_service.domain.service.CancelPayment;
import com.goggles.payment_service.domain.service.CancelResult;
import com.goggles.payment_service.infrastructure.toss.client.TossClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class TossCancelPayment implements CancelPayment {

    private final TossClient client;

    @Override
    public CancelResult cancel(PaymentId paymentId, String paymentKey, String cancelReason) {

        // 멱등성키
        String idempotencyKey = paymentId.getId().toString() + "-cancel";

        ResponseEntity<JsonNode> res = client.cancel(paymentKey, idempotencyKey, Map.of(
            "cancelReason", cancelReason
        ));

        JsonNode node = res.getBody();
        if (res.getStatusCode().is2xxSuccessful()) { // 취소 성공
            log.info("토스 결제 취소 성공 - HTTP 상태코드: {}, 결제 ID: {}, Payment Key: {}", res.getStatusCode(), paymentId.getId(), paymentKey);

            return CancelResult.builder()
                    .success(true)
                    .paymentLog(node == null ? null : node.toString())
                    .build();
        }

        // 취소 실패
        String reason = node.get("code") == null ? "[Unknown]알수없는 예외발생": "[%s]%s".formatted(node.get("code").asText(), node.get("message").asText());
        log.error("토스 결제 취소 실패 - HTTP 상태코드: {}, 결제 ID: {}, Payment Key: {}, 사유: {}", res.getStatusCode(), paymentId.getId(), paymentKey, reason);

        return CancelResult.builder()
                .success(false)
                .reason(reason)
                .build();
    }
}
