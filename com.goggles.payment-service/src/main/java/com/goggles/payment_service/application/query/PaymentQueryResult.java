package com.goggles.payment_service.application.query;

import com.goggles.payment_service.domain.Payment;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record PaymentQueryResult(
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
    public static PaymentQueryResult from(Payment payment) {
        return PaymentQueryResult.builder()
                .paymentId(payment.getPaymentId().getId())
                .status(payment.getStatus().name())
                .orderId(payment.getOrderDetail().getOrderId())
                .amount(payment.getOrderDetail().getOrderPrice())
                .orderName(payment.getOrderDetail().getProductName())
                .paymentKey(payment.getPaymentDetail() == null ? null : payment.getPaymentDetail().getPaymentKey())
                .paymentMethod(payment.getPaymentDetail() == null ? null : payment.getPaymentDetail().getMethod().name())
                .paidAt(payment.getPaymentDetail() == null ? null : payment.getPaymentDetail().getPaidAt())
                .cancelReason(payment.getCancelDetail() == null ? null : payment.getCancelDetail().getReason())
                .canceledAt(payment.getCancelDetail() == null ? null : payment.getCancelDetail().getCanceledAt())
                .build();
    }
}
