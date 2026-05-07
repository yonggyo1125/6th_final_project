package com.goggles.payment_service.domain.service;

import com.goggles.payment_service.domain.PaymentStatus;

import java.time.LocalDateTime;

public record ApproveResult(
        boolean success,
        String reason, // 실패시 사유
        String transactionId,
        PaymentStatus status,
        LocalDateTime paidAt, // 승인 시간
        long approvedAmount, // 실제 결제된 긍액
        String paymentLog
) {}
