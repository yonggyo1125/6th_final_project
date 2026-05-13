package com.goggles.payment_service.infrastructure.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goggles.common.event.annotation.IdempotentConsumer;
import com.goggles.payment_service.application.PaymentService;
import com.goggles.payment_service.application.dto.PaymentServiceDto;
import com.goggles.payment_service.infrastructure.listener.dto.OrderPaymentPending;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPaymentPendingListener {
    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;

    @IdempotentConsumer("topics-order-payment-pending")
    @KafkaListener(topics = "${topics.order.payment-pending}", groupId = "payment-service")
    public void onReceived(Message<String> message, Acknowledgment ack) throws Exception {
        log.info("메세지 수신 - 메세지: {}", message.getPayload());

        try {
            OrderPaymentPending data = objectMapper.readValue(message.getPayload(), OrderPaymentPending.class);

            paymentService.createPayment(
                    PaymentServiceDto.Create.builder()
                            .orderId(data.orderId())
                            .orderPrice(data.amount())
                            .productName(data.orderName())
                            .customerId(data.customerId())
                            .customerName(data.customerName())
                            .customerEmail(data.customerEmail())
                            .build()
            );

            ack.acknowledge(); // 메세지의 offset 적용

            log.info("결제 생성 성공 - 주문 ID: {}, 주문상품: {}, 결제금액: {}", data.orderId(), data.orderName(), data.amount());

        } catch (Exception e) {
            log.error("결제 생성 실패 - 사유: {}", e.getMessage(), e);
            throw e;
        }
    }

    @KafkaListener(topics = "${topics.order.payment-pending}-dlt", groupId = "payment-service")
    public void handleDLT(Message<String> message, Acknowledgment ack) {
        log.error("결제 생성 완전실패, DLT 수신 - 메세지: {}", message.getPayload());
        // 추후 수동처리를 위한 기록 처리..

        ack.acknowledge();
    }
}
