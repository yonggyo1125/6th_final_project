package com.goggles.user_service.user.infrastructure.message;

import com.goggles.user_service.user.domain.service.MessageProvider;
import org.springframework.stereotype.Component;

@Component
public class MessageProviderImpl implements MessageProvider {
    @Override
    public String getMessage(String code, Object... args) {
        return null;
    }
}
