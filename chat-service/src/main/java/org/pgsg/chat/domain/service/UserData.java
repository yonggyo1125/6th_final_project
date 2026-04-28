package org.pgsg.chat.domain.service;

import java.util.UUID;

public record UserData(
        UUID id,
        String nickname
) {}
