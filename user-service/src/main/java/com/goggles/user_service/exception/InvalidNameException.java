package com.goggles.user_service.exception;

import com.goggles.common.exception.BadRequestException;

public class InvalidNameException extends BadRequestException {
    public InvalidNameException(String name) {
        super("유효하지 않은 이름입니다. name=" + name);
    }
}
