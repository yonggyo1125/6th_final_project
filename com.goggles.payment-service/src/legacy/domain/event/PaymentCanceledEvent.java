package legacy.domain.event;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCanceledEvent extends DomainEvent {

  private UUID orderId;

  public PaymentCanceledEvent(String aggregateId, String correlationId, UUID orderId) {
    super("PAYMENT_CANCELED", aggregateId, correlationId);
    this.orderId = orderId;
  }
}
