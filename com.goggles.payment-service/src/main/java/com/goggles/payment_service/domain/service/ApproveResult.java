package com.goggles.payment_service.domain.service;

import com.goggles.payment_service.domain.PaymentStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ApproveResult(
        boolean success,
        String reason, // 실패시 사유
        String paymentKey,
        PaymentStatus status,
        String method,
        LocalDateTime paidAt, // 승인 시간
        long approvedAmount, // 실제 결제된 긍액
        String paymentLog
) {}
