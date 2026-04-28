package com.goggles.user_service.user.domain.exception;

public interface ErrorMessage {
    String getMessage(String code, Object... args);
    String getMessage(String code);
}
