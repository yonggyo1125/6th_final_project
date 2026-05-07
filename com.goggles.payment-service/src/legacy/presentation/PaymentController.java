package legacy.presentation;

import legacy.application.PaymentService;
import legacy.domain.Payment;
import legacy.presentation.dto.ConfirmPaymentRequest;
import legacy.presentation.dto.CreatePaymentRequest;
import legacy.presentation.dto.PaymentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

  private final PaymentService paymentService;

  // 결제 생성
  @PostMapping
  public ResponseEntity<PaymentResponse> createPayment(
      @Valid @RequestBody CreatePaymentRequest request) {
    Payment payment =
        paymentService.createPayment(
            request.getOrderId(), request.getAmount());
    return ResponseEntity.ok(PaymentResponse.from(payment));
  }

  // 결제 승인
  @PostMapping("/{paymentId}/confirm")
  public ResponseEntity<PaymentResponse> confirmPayment(
      @PathVariable UUID paymentId, @Valid @RequestBody ConfirmPaymentRequest request) {
    Payment payment = paymentService.confirmPayment(paymentId, request.getPaymentKey());
    return ResponseEntity.ok(PaymentResponse.from(payment));
  }

  // 결제 취소
  @PostMapping("/{paymentId}/cancel")
  public ResponseEntity<PaymentResponse> cancelPayment(
      @PathVariable UUID paymentId, @RequestParam String cancelReason) {
    Payment payment = paymentService.cancelPayment(paymentId, cancelReason);
    return ResponseEntity.ok(PaymentResponse.from(payment));
  }
}
