package com.goggles.payment_service.domain.event;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestedEvent extends DomainEvent {

  private UUID orderId;
  private Long amount;

  public PaymentRequestedEvent(
      String aggregateId, String correlationId, UUID orderId, Long amount) {
    super("PAYMENT_REQUESTED", aggregateId, correlationId);
    this.orderId = orderId;
    this.amount = amount;
  }
}
