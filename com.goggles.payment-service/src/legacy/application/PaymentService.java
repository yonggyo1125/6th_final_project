package legacy.application;

import legacy.domain.Payment;

import java.util.UUID;

public interface PaymentService {

  Payment createPayment(UUID orderID, Long amount);

  Payment confirmPayment(UUID paymentID, String paymentKey);

  Payment cancelPayment(UUID paymentId, String cancelReason);
}
