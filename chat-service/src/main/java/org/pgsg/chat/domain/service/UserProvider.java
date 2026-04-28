package org.pgsg.chat.domain.service;

import java.util.UUID;

public interface UserProvider {
    UserData getUser(UUID userId);
}
