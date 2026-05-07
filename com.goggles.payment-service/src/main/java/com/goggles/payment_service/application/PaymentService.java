package com.goggles.payment_service.application;

import com.goggles.payment_service.domain.Payment;

import java.util.UUID;

public interface PaymentService {

  Payment createPayment(UUID orderID, Long amount);

  Payment confirmPayment(UUID paymentID, String paymentKey);

  Payment cancelPayment(UUID paymentId, String cancelReason);
}
