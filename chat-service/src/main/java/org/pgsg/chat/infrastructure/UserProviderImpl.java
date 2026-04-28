package org.pgsg.chat.infrastructure;

import org.pgsg.chat.domain.service.UserData;
import org.pgsg.chat.domain.service.UserProvider;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserProviderImpl implements UserProvider {
    @Override
    public UserData getUser(UUID userId) {
        return null;
    }
}
