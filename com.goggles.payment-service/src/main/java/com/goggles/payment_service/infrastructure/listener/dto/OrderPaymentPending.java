package com.goggles.payment_service.infrastructure.listener.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderPaymentPending(
        UUID orderId,
        long amount,
        String orderName,
        UUID customerId,
        String customerName,
        String customerEmail
) {}
