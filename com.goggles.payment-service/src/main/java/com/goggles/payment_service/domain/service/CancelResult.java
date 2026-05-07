package com.goggles.payment_service.domain.service;

public record CancelResult(
        boolean success,
        String reason,
        String paymentLog
) {}
