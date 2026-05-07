package com.goggles.payment_service.domain.service;

public interface CancelPayment {

  CancelResult cancel(String paymentId, String paymentKey, String cancelReason);
}
