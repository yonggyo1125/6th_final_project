package org.sparta.fileservice.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.sparta.fileservice.domain.service.RoleCheck;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityRoleCheck implements RoleCheck {
    @Override
    public boolean isMaster() {
        return true; // 임시
    }

    @Override
    public boolean isMine(String fileOwner) {
        return true; // 임시
    }

    @Override
    public boolean isLoggedIn() {
        return true; // 임시
    }
}
