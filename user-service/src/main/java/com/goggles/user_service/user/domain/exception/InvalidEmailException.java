package com.goggles.user_service.user.domain.exception;

import com.goggles.common.exception.BadRequestException;

public class InvalidEmailException extends BadRequestException {
    public InvalidEmailException(String email) {
        super("유효하지 않는 email입니다 email=" + email);
    }
}
