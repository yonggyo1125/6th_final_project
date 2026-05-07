package com.goggles.payment_service.domain.exception;

import com.goggles.common.exception.BadRequestException;

public class PaymentInvalidException extends BadRequestException {
    public PaymentInvalidException(String message) {
        super(message);
    }
}
