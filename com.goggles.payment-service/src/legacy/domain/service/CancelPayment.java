package legacy.domain.service;

public interface CancelPayment {

  CancelResult cancel(String paymentId, String paymentKey, String cancelReason);
}
