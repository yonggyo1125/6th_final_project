package com.goggles.payment_service.infrastructure.toss.topic;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="topics.payment")
public record PaymentTopic(
        String confirmed,
        String confirmFailed,
        String canceled,
        String cancelFailed
) {}
