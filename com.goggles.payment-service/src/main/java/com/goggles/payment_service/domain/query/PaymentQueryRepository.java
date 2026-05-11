package com.goggles.payment_service.domain.query;

import com.goggles.payment_service.domain.Payment;

import java.util.Optional;
import java.util.UUID;

public interface PaymentQueryRepository {
    Optional<Payment> findByOrderId(UUID orderId);
}
