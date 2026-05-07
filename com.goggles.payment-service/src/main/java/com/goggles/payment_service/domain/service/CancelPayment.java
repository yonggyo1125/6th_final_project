package com.goggles.payment_service.domain.service;

import com.goggles.payment_service.domain.PaymentId;

public interface CancelPayment {
    CancelResult cancel(PaymentId paymentId, String transactionId, String cancelReason);
}
