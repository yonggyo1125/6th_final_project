package com.goggles.payment_service.infrastructure.toss;

import com.fasterxml.jackson.databind.JsonNode;
import com.goggles.payment_service.domain.OrderDetail;
import com.goggles.payment_service.domain.PaymentId;
import com.goggles.payment_service.domain.PaymentStatus;
import com.goggles.payment_service.domain.service.ApprovePayment;
import com.goggles.payment_service.domain.service.ApproveResult;
import com.goggles.payment_service.infrastructure.toss.client.TossClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class TossApprovePayment implements ApprovePayment {

    private final TossClient tossClient;

    @Override
    public ApproveResult request(PaymentId paymentId, String paymentKey, OrderDetail orderDetail) {
        // 멱등키
        String idempotencyKey = paymentId.getId().toString() + "-approve";

        // 주문 ID
        String orderId = orderDetail.getOrderId().toString();

        ResponseEntity<JsonNode> res = tossClient.approve(idempotencyKey, Map.of(
                "paymentKey", paymentKey,
                "orderId", orderId,
                "amount", orderDetail.getOrderPrice()
        ));

        // 응답 바디
        JsonNode node = res.getBody();

        if (res.getStatusCode().is2xxSuccessful()) { // 성공
            // 결제 상태
            PaymentStatus status = PaymentStatus.valueOf(node.get("status").asText());

            // 승인 일시
            LocalDateTime paidAt = LocalDateTime.parse(node.get("approvedAt").asText());

            // 승인된 결제금액
            long approvedAmount = node.get("totalAmount").asLong();

            // 결제 수단
            String method = node.get("method").asText();

            log.info("토스 결제 승인 성공 - HTTP 상태코드: {}, 주문 ID: {}, 결제 ID: {}, Payment Key: {}, 결제금액: {}, 결제 수단: {}, 승인 상태: {}, 승인 일시: {}", res.getStatusCode(), orderId, paymentId.getId(), paymentKey, approvedAmount, method, "%s(%s)".formatted(status, status.getDescription()), paidAt);

            return ApproveResult.builder()
                    .success(true)
                    .status(status)
                    .paidAt(paidAt)
                    .approvedAmount(approvedAmount)
                    .paymentKey(paymentKey)
                    .method(method)
                    .paymentLog(node.toString())
                    .build();
        }

        // 실패
        String reason = node.get("code") == null ? "[Unknown]알수없는 예외발생": "[%s]%s".formatted(node.get("code").asText(), node.get("message").asText());

        log.error("토스 결제 승인 실패 - HTTP 상태코드: {}, 주문 ID: {}, 결제 ID: {}, Payment Key: {}, 결제금액: {}, 사유: {}", res.getStatusCode(), orderId, paymentId.getId(), paymentKey, orderDetail.getOrderPrice(), reason);

        return ApproveResult.builder()
                .success(false)
                .reason(reason)
                .build();
    }
}
