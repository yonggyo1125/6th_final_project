package com.goggles.user_service.user.infrastructure.keycloak;

import com.goggles.user_service.user.domain.service.IdentityProvider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@Slf4j
@SpringBootTest
public class KeycloakIdentityProviderTest {

    @Autowired
    IdentityProvider identityProvider;

    @Test
    @DisplayName("키클록 사용자 등록 테스트")
    void keycloak_user_register_test() {
        UUID userId = identityProvider.register("test03@test.org", "_aA123456");
        log.info("userId: {}", userId);
    }
}
