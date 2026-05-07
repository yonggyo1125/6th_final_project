package com.goggles.payment_service.domain.service;

public interface ApprovePayment {
  ApproveResult request(String paymentId, String paymentKey, String orderId, Long amount);
}
