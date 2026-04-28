package com.goggles.user_service.user.domain.exception;

import com.goggles.common.exception.BadRequestException;

public class InvalidInstructorDescriptionException extends BadRequestException {
    public InvalidInstructorDescriptionException(String value) {
        super("누락된 정보가 있습니다. missingInfo=" + value);
    }
}
