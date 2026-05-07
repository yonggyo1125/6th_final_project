package com.goggles.payment_service.application;

import com.goggles.common.event.Events;
import com.goggles.payment_service.domain.Payment;
import com.goggles.payment_service.domain.PaymentMethod;
import com.goggles.payment_service.domain.PaymentRepository;
import com.goggles.payment_service.domain.PaymentStatus;
import com.goggles.payment_service.domain.event.PaymentCanceledEvent;
import com.goggles.payment_service.domain.event.PaymentCompletedEvent;
import com.goggles.payment_service.domain.event.PaymentFailedEvent;
import com.goggles.payment_service.domain.event.PaymentRequestedEvent;
import com.goggles.payment_service.domain.exception.DuplicatePaymentException;
import com.goggles.payment_service.domain.exception.PaymentNotFoundException;
import com.goggles.payment_service.domain.service.ApprovePayment;
import com.goggles.payment_service.domain.service.ApproveResult;
import com.goggles.payment_service.domain.service.CancelPayment;
import com.goggles.payment_service.domain.service.CancelResult;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

  private final PaymentRepository paymentRepository;
  private final ApprovePayment approvePayment;
  private final CancelPayment cancelPaymentService;
  private final Events events;

  // 결제 생성 (READY)
  @Transactional
  public Payment createPayment(UUID orderId, Long amount) {
    // 중복 결제 방지
    if (paymentRepository.existsByOrderIdAndStatus(orderId, PaymentStatus.SUCCESS)) {
      throw new DuplicatePaymentException(orderId);
    }

    Payment payment = Payment.create(orderId, amount);
    paymentRepository.save(payment);

    events.trigger(
        payment.getOrderId().toString(),
        "PAYMENT",
        "payment-requested",
        new PaymentRequestedEvent(
            payment.getId().toString(), payment.getOrderId().toString(), orderId, amount));

    return payment;
  }

  // 결제 승인 (READY -> SUCCESS)
  @Transactional
  public Payment confirmPayment(UUID paymentId, String paymentKey) {
    Payment payment =
        paymentRepository
            .findById(paymentId)
            .orElseThrow(() -> new PaymentNotFoundException(paymentId));

    ApproveResult result =
        approvePayment.request(
            paymentId.toString(),
            paymentKey,
            payment.getOrderId().toString(),
            payment.getAmount().getAmount());

    if (result.isSuccess()) {
      payment.success(paymentKey, result.getPaymentLog());

      events.trigger(
          payment.getOrderId().toString(),
          "PAYMENT",
          "payment-completed",
          new PaymentCompletedEvent(
              payment.getId().toString(),
              payment.getOrderId().toString(),
              payment.getOrderId(),
              payment.getAmount().getAmount(),
              payment.getPaidAt()));
    } else {
      payment.fail(paymentKey, result.getFailReason(), result.getPaymentLog());

      events.trigger(
          payment.getOrderId().toString(),
          "PAYMENT",
          "payment-failed",
          new PaymentFailedEvent(
              payment.getId().toString(),
              payment.getOrderId().toString(),
              payment.getOrderId(),
              payment.getFailReason()));
    }

    return payment;
  }

  // 결제 취소 (SUCCESS -> CANCEL)
  @Transactional
  public Payment cancelPayment(UUID paymentId, String cancelReason) {
    Payment payment =
        paymentRepository
            .findById(paymentId)
            .orElseThrow(() -> new PaymentNotFoundException(paymentId));

    CancelResult cancelResult =
        cancelPaymentService.cancel(paymentId.toString(), payment.getTransactionId(), cancelReason);

    if (!cancelResult.isSuccess()) {
      throw new IllegalStateException("결제 취소 실패: " + cancelResult.getFailReason());
    }

    payment.cancel(cancelResult.getPaymentLog());

    events.trigger(
        payment.getOrderId().toString(),
        "PAYMENT",
        "payment-canceled",
        new PaymentCanceledEvent(
            payment.getId().toString(), payment.getOrderId().toString(), payment.getOrderId()));

    return payment;
  }
}
