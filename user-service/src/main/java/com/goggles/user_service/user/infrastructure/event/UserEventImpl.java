package com.goggles.user_service.user.infrastructure.event;

import com.goggles.user_service.user.domain.User;
import com.goggles.user_service.user.domain.event.UserEvent;
import org.springframework.stereotype.Component;

@Component
public class UserEventImpl implements UserEvent {
    @Override
    public void changed(User user) {

    }
}
