package com.goggles.payment_service.domain.service;

import com.goggles.payment_service.domain.Payment;

public interface RoleChecker {
    boolean isMaster();
    boolean isMine(Payment payment);
}
