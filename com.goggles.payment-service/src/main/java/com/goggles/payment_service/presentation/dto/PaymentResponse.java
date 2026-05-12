package com.goggles.payment_service.presentation.dto;

import com.goggles.payment_service.application.query.PaymentQueryResult;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentResponse {

    @Builder
    public record PaymentInfo(
            UUID paymentId,
            String status,
            UUID orderId,
            long amount,
            String orderName,
            String paymentKey,
            String paymentMethod,
            LocalDateTime paidAt,
            String cancelReason,
            LocalDateTime canceledAt
    ) {
        public static PaymentInfo from(PaymentQueryResult result) {
            return PaymentInfo.builder()
                    .paymentId(result.paymentId())
                    .status(result.status())
                    .orderId(result.orderId())
                    .amount(result.amount())
                    .orderName(result.orderName())
                    .paymentKey(result.paymentKey())
                    .paymentMethod(result.paymentMethod())
                    .paidAt(result.paidAt())
                    .cancelReason(result.cancelReason())
                    .canceledAt(result.canceledAt())
                    .build();
        }
    }

    public record PaymentApprove(
            UUID paymentId
    ) {}

    public record PaymentCancel(
            UUID paymentId
    ) {}
}
