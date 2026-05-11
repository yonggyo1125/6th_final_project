package com.goggles.payment_service.domain.exception;

import com.goggles.common.exception.NotFoundException;
import java.util.UUID;

public class PaymentNotFoundException extends NotFoundException {

  public PaymentNotFoundException(UUID paymentId) {
    super("결제를 찾을 수 없습니다. paymentId: " + paymentId);
  }
}
