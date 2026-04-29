package com.goggles.user_service.user.infrastructure.keycloak;

import com.goggles.user_service.user.domain.UserId;
import com.goggles.user_service.user.domain.exception.DuplicatedUserException;
import com.goggles.user_service.user.domain.exception.IdentityProviderException;
import com.goggles.user_service.user.domain.exception.UserNotFoundException;
import com.goggles.user_service.user.domain.service.IdentityProvider;
import com.goggles.user_service.user.domain.service.MessageProvider;
import com.goggles.user_service.user.infrastructure.keycloak.config.KeycloakProperties;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeycloakIdentityProvider implements IdentityProvider {

    private final Keycloak keycloak;
    private final KeycloakProperties properties;
    private final MessageProvider messageProvider;

    @Override
    public UUID register(String email, String password) {
        UserRepresentation user = new UserRepresentation();
        // 회원 기본 정보
        user.setEnabled(true);
        user.setEmail(email);
        user.setEmailVerified(true);
        user.setUsername(email);
        user.setRequiredActions(List.of());

        // 비밀번호
        CredentialRepresentation credential = getCredential(password);
        user.setCredentials(List.of(credential));

        // Keycloak 등록 처리 요청
        UsersResource usersResource = keycloak.realm(properties.realm()).users();
        try (Response response = usersResource.create(user)) {
            // 성공 여부 -> 응답코드: 201(CREATED)
            if (response.getStatus() == HttpStatus.CONFLICT.value()) { // 409
                throw new DuplicatedUserException(messageProvider.getMessage("user.registration.duplicated"));
            }

            if (response.getStatus() != HttpStatus.CREATED.value()) {
                log.error("Keycloak 회원등록 실패: 응답코드: {}", response.getStatus());

                throw new IdentityProviderException(messageProvider.getMessage("user.registration.failed"));
            }

            // 등록 성공
            // 응답 헤더 : Location: 리다이렉트 경로/UUID/....
            if (response.getLocation() == null) {
                log.error("keyclock 응답 헤더에 Location 누락");
                throw new IdentityProviderException(messageProvider.getMessage("user.registration.userId.missing"));
            }

            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

            return UUID.fromString(userId);
        }
    }

    @Override
    public void withdraw(UUID id) {
        try {
            keycloak.realm(properties.realm()).users()
                    .get(id.toString())
                    .remove();
        } catch (Exception e) {
            log.error("keycloak 회원 삭제 실패 - ID: {}, 사유: {}", id, e.getMessage(), e);
        }
    }

    @Override
    public void changePassword(UserId id, String newPassword) {
        UsersResource usersResource = keycloak.realm(properties.realm()).users();
        String userId = id.getId().toString();
        try {
            usersResource.get(userId).resetPassword(getCredential(newPassword));
        } catch (NotFoundException e) {
            log.error("keycloak에서 사용자를 찾을 수 없음 - ID: {}, 사유: {}", userId, e.getMessage(), e);
            throw new UserNotFoundException(messageProvider.getMessage("user.notfound"));
        } catch (Exception e) {
            log.error("keycloak에서 비밀번호 변경 실패 - ID: {}, 사유: {}", userId, e.getMessage(), e);
            throw new IdentityProviderException(messageProvider.getMessage("user.changepassword.failed"));
        }
    }

    private CredentialRepresentation getCredential(String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false); // true -> 최초 로그인 시 비번 변경이 필수, 기본값

        return credential;
    }
}
