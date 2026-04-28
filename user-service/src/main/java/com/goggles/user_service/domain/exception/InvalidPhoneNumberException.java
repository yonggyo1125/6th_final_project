package com.goggles.user_service.domain.exception;

import com.goggles.common.exception.BadRequestException;

public class InvalidPhoneNumberException extends BadRequestException {
    public InvalidPhoneNumberException(String phoneNumber) {
        super("유효하지 않은 전화번호 입니다. phoneNumber=" + phoneNumber);
    }
}
