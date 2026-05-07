package com.goggles.payment_service.domain;

import legacy.domain.Payment;

import java.util.UUID;

public class PaymentFixture {

  public static final UUID ORDER_ID = UUID.randomUUID();
  public static final Long AMOUNT = 50000L;

  public static Payment createReadyPayment() {
    return Payment.create(ORDER_ID, AMOUNT);
  }

  public static Payment createSuccessPayment() {
    Payment payment = Payment.create(ORDER_ID, AMOUNT);
    payment.success("test_transaction_key", null);
    return payment;
  }

  public static Payment createFailPayment() {
    Payment payment = Payment.create(ORDER_ID, AMOUNT);
    payment.fail("test_transaction_key", "잔액 부족", null);
    return payment;
  }
}
