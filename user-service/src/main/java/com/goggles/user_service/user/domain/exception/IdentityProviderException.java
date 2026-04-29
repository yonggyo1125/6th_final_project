package com.goggles.user_service.user.domain.exception;

import com.goggles.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class IdentityProviderException extends CustomException {
    public IdentityProviderException(String message) {
        super(message, HttpStatus.BAD_GATEWAY);
    }
}
