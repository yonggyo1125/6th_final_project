package com.goggles.user_service.exception;

import com.goggles.common.exception.BadRequestException;

public class InvalidUserIdException extends BadRequestException {
    public InvalidUserIdException(String userId) {
        super("유효하지 않은 userId입니다.");
    }
}
