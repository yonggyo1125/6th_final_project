package org.pgsg.file_service.infrastructure.security;

import org.pgsg.common.util.SecurityUtil;
import org.pgsg.config.security.UserDetailsImpl;
import org.pgsg.file_service.domain.FileInfo;
import org.pgsg.file_service.domain.service.RoleChecker;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SecurityRoleChecker implements RoleChecker {
    @Override
    public boolean isMaster() {
        return isLoggedIn() && getLoggedUser() != null &&
                getLoggedUser().getUserRole().contains("MASTER");
    }

    @Override
    public boolean isMine(FileInfo fileInfo) {

        return getLoggedUserId() != null &&
                fileInfo.getCreatedBy() != null &&
                fileInfo.getCreatedBy().equals(getLoggedUserId());
    }

    @Override
    public boolean isLoggedIn() {

        return getLoggedUser() != null;
    }

    @Override
    public UUID getLoggedUserId() {
        return SecurityUtil.getCurrentUserId().orElse(null);
    }

    private UserDetailsImpl getLoggedUser() {
        return SecurityUtil.getCurrentUser().orElse(null);
    }
}
