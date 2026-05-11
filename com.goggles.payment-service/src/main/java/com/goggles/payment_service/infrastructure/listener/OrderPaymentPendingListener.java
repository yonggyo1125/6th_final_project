package com.goggles.payment_service.infrastructure.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goggles.common.event.Events;
import com.goggles.common.event.annotation.IdempotentConsumer;
import com.goggles.payment_service.application.PaymentService;
import com.goggles.payment_service.infrastructure.listener.dto.OrderPaymentPending;
import com.goggles.payment_service.infrastructure.topic.PaymentTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(PaymentTopic.class)
public class OrderPaymentPendingListener {
    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;
    private final PaymentTopic paymentTopic;
    private final Events events;

    @IdempotentConsumer("topics-order-payment-pending")
    @KafkaListener(topics = "${topics.order.payment-pending}", groupId = "payment-service")
    public void onReceived(Message<String> message, Acknowledgment ack) throws Exception {
        log.info("카프카 메세지 수신 - 메세지: {}", message.getPayload());

        try {
            OrderPaymentPending data = objectMapper.readValue(message.getPayload(), OrderPaymentPending.class);

            paymentService.createPayment(data.orderId(), data.orderName(), data.amount());

            ack.acknowledge(); // 메세지의 offset 적용

            log.info("결제 생성 성공 - 주문 ID: {}, 주문상품: {}, 결제금액: {}", data.orderId(), data.orderName(), data.amount());

            events.trigger(data.orderId().toString(), "payment", paymentTopic.created(), data); // 결제 생성 후 이벤트 발행
            log.info("결제 생성 성공 후 이벤트 발행 - 토픽이름: {}", paymentTopic.created());

        } catch (Exception e) {
            log.error("결제 생성 실패 - 사유: {}", e.getMessage(), e);
            throw e;
        }
    }

    @KafkaListener(topics = "${topics.order.payment-pending}-dlt", groupId = "payment-service")
    public void handleDLT(Message<String> message, Acknowledgment ack) {
        log.error("결제 생성 완전실패, DLT 수신 - 메세지: {}", message.getPayload());
        try {
            OrderPaymentPending data = objectMapper.readValue(message.getPayload(), OrderPaymentPending.class);

            events.trigger(data.orderId().toString(), "payment", paymentTopic.createdFailed(), data);

        } catch (JsonProcessingException e) {
            log.error("DLT 수신 Payload JSON 변환 실패 - 사유: {}", e.getMessage(), e);
        }
        ack.acknowledge();
    }
}
