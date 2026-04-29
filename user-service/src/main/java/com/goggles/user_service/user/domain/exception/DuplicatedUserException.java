package com.goggles.user_service.user.domain.exception;

import com.goggles.common.exception.ConflictException;

public class DuplicatedUserException extends ConflictException {
    public DuplicatedUserException(String message) {
        super(message);
    }
}
