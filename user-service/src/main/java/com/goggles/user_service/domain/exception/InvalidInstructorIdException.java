package com.goggles.user_service.domain.exception;

import com.goggles.common.exception.BadRequestException;

public class InvalidInstructorIdException extends BadRequestException {
    public InvalidInstructorIdException(String instructorId) {
        super("유효하지 않은 instructorId입니다.");
    }
}
