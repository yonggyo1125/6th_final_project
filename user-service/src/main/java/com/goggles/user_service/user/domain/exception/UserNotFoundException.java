package com.goggles.user_service.user.domain.exception;

import com.goggles.common.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }

}
