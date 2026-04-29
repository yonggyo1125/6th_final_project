package com.goggles.user_service.user.infrastructure.security;

import com.goggles.user_service.user.domain.Role;
import com.goggles.user_service.user.domain.service.RoleCheck;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SecurityRoleCheck implements RoleCheck {

    @Override
    public boolean hasRole(List<Role> roles) {
        return false;
    }

    @Override
    public boolean isMine() {
        return false;
    }
}
