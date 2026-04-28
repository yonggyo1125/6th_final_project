package com.goggles.user_service.exception;

import com.goggles.common.exception.BadRequestException;

public class InvalidExperienceException extends BadRequestException {
    public InvalidExperienceException(String value) {
        super("잘못된 정보입니다. value=" + value);
    }
}
