package com.goggles.payment_service.presentation.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentRequest {
    // 결제 성공시 데이터
    public record Success(
            UUID orderId,
            String paymentKey,
            long amount
    ) {}

    // 결제 실패시
    public record Failure(
            String code,
            String message
    ) {}
}
