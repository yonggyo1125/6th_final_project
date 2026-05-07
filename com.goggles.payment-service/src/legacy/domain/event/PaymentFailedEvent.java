package legacy.domain.event;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentFailedEvent extends DomainEvent {

  private UUID orderId;
  private String failReason;

  public PaymentFailedEvent(
      String aggregateId, String correlationId, UUID orderId, String failReason) {
    super("PAYMENT_FAILED", aggregateId, correlationId);
    this.orderId = orderId;
    this.failReason = failReason;
  }
}
