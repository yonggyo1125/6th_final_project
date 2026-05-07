package com.goggles.payment_service.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goggles.common.event.annotation.IdempotentConsumer;
import com.goggles.payment_service.application.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentEventConsumer {

  private final PaymentService paymentService;
  private final ObjectMapper objectMapper;

  // 주문 서비스 이벤트 포틱 확정 후 수정 필요
  @KafkaListener(topics = "order-payment-requested", groupId = "payment-service")
  @IdempotentConsumer("order-payment-requested")
  public void handlePaymentRequested(ConsumerRecord<String, String> record) {
    try {
      log.info("주문 결제 요청 이벤트 수신, key: {}", record.key());

      OrderEventDto event = objectMapper.readValue(record.value(), OrderEventDto.class);

      if (event.getOrderID() == null) {
        throw new IllegalArgumentException("orderId는 필수입니다.");
      }
      if (event.getAmount() == null) {
        throw new IllegalArgumentException("amount는 0원 이상이어야 합니다.");
      }

      paymentService.createPayment(event.getOrderID(), event.getAmount());

    } catch (Exception e) {
      log.error("주문 결제 요청 이벤트 처리 실패, key: {}, error: {}", record.key(), e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
