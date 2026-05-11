package com.goggles.payment_service.infrastructure.persistence;

import com.goggles.payment_service.domain.Payment;
import com.goggles.payment_service.domain.PaymentId;
import com.goggles.payment_service.domain.PaymentRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPaymentRepository extends PaymentRepository, JpaRepository<Payment, PaymentId> {
}
