package com.goggles.payment_service.domain.service;

import lombok.Builder;

@Builder
public record CancelResult(
        boolean success,
        String reason,
        String paymentLog
) {}
