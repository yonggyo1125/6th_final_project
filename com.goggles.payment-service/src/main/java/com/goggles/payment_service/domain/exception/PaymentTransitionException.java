package com.goggles.payment_service.domain.exception;

import com.goggles.common.exception.BadRequestException;
import com.goggles.payment_service.domain.PaymentStatus;

public class PaymentTransitionException extends BadRequestException {
    public PaymentTransitionException(PaymentStatus currentStatus, PaymentStatus newStatus) {
        super("유효하지 않은 결제 상태 변경입니다 - 현재상태: %s, 변경될 상태: %s".formatted(currentStatus, newStatus));
    }
}
