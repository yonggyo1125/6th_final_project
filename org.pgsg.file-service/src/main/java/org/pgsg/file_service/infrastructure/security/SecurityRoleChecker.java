package org.pgsg.file_service.infrastructure.security;

import org.pgsg.file_service.domain.FileInfo;
import org.pgsg.file_service.domain.service.RoleChecker;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SecurityRoleChecker implements RoleChecker {
    @Override
    public boolean isMaster() {
        return true;
    }

    @Override
    public boolean isMine(FileInfo fileInfo) {
        return true;
    }

    @Override
    public boolean isLoggedIn() {
        return true;
    }

    @Override
    public UUID getLoggedUserId() {
        return UUID.randomUUID();
    }
}
