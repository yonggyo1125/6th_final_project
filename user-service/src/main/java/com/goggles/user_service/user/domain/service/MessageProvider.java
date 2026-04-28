package com.goggles.user_service.user.domain.service;

public interface MessageProvider {
    String getMessage(String code, Object... args);
    default String getMessage(String code) {
        return getMessage(code, (Object[]) null);
    }
}
