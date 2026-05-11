package com.goggles.payment_service.domain.event.dto;

import com.goggles.payment_service.domain.Payment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentEventDto {

    public record Created(
            UUID orderId,
            LocalDateTime createdAt
    ) {}

    public record CreateFailed(
            UUID orderId,
            LocalDateTime failedAt,
            LocalDateTime failReason
    ) {}

    // 승인 완료
    public record Approved(
            UUID orderId,
            String paymentKey,
            long amount,
            LocalDateTime approvedAt
    ) {}

    // 승인 실패
    @Builder
    public record ApprovalFailed(
        UUID orderId,
        long amount,
        LocalDateTime failedAt,
        String failReason
    ) {
        public static ApprovalFailed from(Payment payment, String failReason) {
            return ApprovalFailed.builder()
                    .orderId(payment.getOrderDetail().getOrderId())
                    .amount(payment.getOrderDetail().getOrderPrice())
                    .failedAt(LocalDateTime.now())
                    .failReason(failReason)
                    .build();
        }
    }

    // 결제 취소 완료
    public record Cancelled(
            UUID orderId,
            long amount,
            LocalDateTime cancelledAt,
            String cancelReason
    ) {}

    // 결제 취소 실패
    @Builder
    public record CancelFailed(
            UUID orderId,
            long amount,
            LocalDateTime failedAt,
            String failReason
    ) {
        public static CancelFailed from(Payment payment, String failReason) {
            return CancelFailed.builder()
                    .orderId(payment.getOrderDetail().getOrderId())
                    .amount(payment.getOrderDetail().getOrderPrice())
                    .failedAt(LocalDateTime.now())
                    .failReason(failReason)
                    .build();
        }
    }
}
