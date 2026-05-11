package com.goggles.payment_service.infrastructure.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goggles.common.event.Events;
import com.goggles.payment_service.application.PaymentService;
import com.goggles.payment_service.infrastructure.topic.PaymentTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(PaymentTopic.class)
public class OrderPaymentCancelListener {
    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;
    private final PaymentTopic paymentTopic;
    private final Events events;


}
