package com.goggles.payment_service.domain.exception;

import com.goggles.common.exception.ConflictException;
import java.util.UUID;

public class PaymentDuplicatedException extends ConflictException {

  public PaymentDuplicatedException(UUID orderId) {
    super("이미 등록된 결제입니다. orderId = " + orderId);
  }
}
