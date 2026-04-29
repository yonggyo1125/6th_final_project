package com.goggles.user_service.user.infrastructure.keycloak;

import com.goggles.user_service.user.domain.UserId;
import com.goggles.user_service.user.domain.service.IdentityProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeycloakIdentityProvider implements IdentityProvider {
    @Override
    public UUID register(String email, String password) {
        return null;
    }

    @Override
    public void withdraw(UUID id) {

    }

    @Override
    public void changePassword(UserId id, String newPassword) {

    }
}
