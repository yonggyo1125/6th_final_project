package com.goggles.user_service.user.domain.exception;

import com.goggles.common.exception.BadRequestException;

public class InvalidBankAccountException extends BadRequestException {
    public InvalidBankAccountException(String value) {
        super("유효하지 않은 은행정보입니다. value=" + value);
    }
}
