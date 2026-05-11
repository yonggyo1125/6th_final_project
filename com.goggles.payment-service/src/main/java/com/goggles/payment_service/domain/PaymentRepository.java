package com.goggles.payment_service.domain;

import java.util.Optional;

public interface PaymentRepository {
    Payment save(Payment payment);
    Optional<Payment> findById(PaymentId paymentId);
}
