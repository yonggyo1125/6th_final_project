package com.goggles.payment_service.infrastructure.topic;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="topics.payment")
public record PaymentTopic(
        String created,
        String createdFailed,
        String confirmed,
        String confirmFailed,
        String canceled,
        String cancelFailed
) {}
