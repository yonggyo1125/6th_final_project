package com.goggles.payment_service.domain.service;

import java.util.UUID;

public interface OrderChecker {
    boolean isDuplicated(UUID orderId);
}
