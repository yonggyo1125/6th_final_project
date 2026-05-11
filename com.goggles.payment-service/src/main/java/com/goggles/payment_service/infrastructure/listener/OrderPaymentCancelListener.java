package com.goggles.payment_service.infrastructure.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goggles.common.event.annotation.IdempotentConsumer;
import com.goggles.payment_service.application.PaymentService;
import com.goggles.payment_service.domain.Payment;
import com.goggles.payment_service.domain.query.PaymentQueryRepository;
import com.goggles.payment_service.infrastructure.listener.dto.OrderPaymentCancel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPaymentCancelListener {
    private final PaymentQueryRepository queryRepository;
    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;

    @IdempotentConsumer("topics-order-payment-cancel")
    @KafkaListener(topics = "${topics.order.payment-cancel}", groupId = "payment-service")
    public void onReceived(Message<String> message, Acknowledgment ack) throws Exception {
        log.info("메시지 수신 - 메시지: {}", message.getPayload());

        try {
            OrderPaymentCancel data =  objectMapper.readValue(message.getPayload(), OrderPaymentCancel.class);
            UUID orderId = data.orderId(); // 주문ID로 결제 정보 조회
            String reason = "[%s]%s".formatted(data.cancelReason(), data.cancelDescription());

            Payment payment = OrderPaymentHelper.getPayment(orderId, queryRepository);

            paymentService.cancelPayment(payment.getPaymentId().getId(), reason);
            ack.acknowledge();

            log.info("결제 취소 성공 - 결제 ID: {}, 주문 ID: {}", payment.getPaymentId().getId(), orderId);

        } catch (Exception e) {
            log.error("결제 취소 실패 - 사유: {}", e.getMessage(), e);
            throw e;
        }
    }

    @KafkaListener(topics = "${topics.order.payment-cancel}-dlt", groupId = "payment-service")
    public void handleDLT(Message<String> message, Acknowledgment ack) {
        log.error("결제 취소 완전 실패, DLT 수신 - 메세지: {}",  message.getPayload());

        // 수동처리를 위한 기록...

        ack.acknowledge();
    }

}
