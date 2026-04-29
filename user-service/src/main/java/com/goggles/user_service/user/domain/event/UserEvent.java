package com.goggles.user_service.user.domain.event;

import com.goggles.user_service.user.domain.User;

public interface UserEvent {
    void changed(User user);
}
