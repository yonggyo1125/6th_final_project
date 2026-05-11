package com.goggles.payment_service.infrastructure.listener.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderPaymentCancel(
        UUID orderId,
        String cancelReason,
        String cancelDescription
) {}
