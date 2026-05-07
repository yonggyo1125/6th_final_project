package legacy.presentation.dto;

import legacy.domain.Payment;
import legacy.domain.PaymentMethod;
import legacy.domain.PaymentStatus;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;

@Getter
public class PaymentResponse {

  private final UUID id;
  private final UUID orderId;
  private final Long amount;
  private final PaymentStatus status;
  private final PaymentMethod method;
  private final String transactionId;
  private final String failReason;
  private final LocalDateTime paidAt;
  private final String paymentLog;

  private PaymentResponse(Payment payment) {
    this.id = payment.getId();
    this.orderId = payment.getOrderId();
    this.amount = payment.getAmount().getAmount();
    this.status = payment.getStatus();
    this.method = payment.getMethod();
    this.transactionId = payment.getTransactionId();
    this.failReason = payment.getFailReason();
    this.paidAt = payment.getPaidAt();
    this.paymentLog = payment.getPaymentLog();
  }

  public static PaymentResponse from(Payment payment) {
    return new PaymentResponse(payment);
  }
}
