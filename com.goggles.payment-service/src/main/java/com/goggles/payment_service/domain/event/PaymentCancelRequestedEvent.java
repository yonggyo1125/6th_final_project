package com.goggles.payment_service.domain.event;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCancelRequestedEvent extends DomainEvent {

  private UUID orderId;
  private String transactionId;

  public PaymentCancelRequestedEvent(
      String aggregateID, String correlationID, UUID orderId, String transactionId) {
    super("PAYMENT_CANCEL_REQUESTED", aggregateID, correlationID);
    this.orderId = orderId;
    this.transactionId = transactionId;
  }
}
