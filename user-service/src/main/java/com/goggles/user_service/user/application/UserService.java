package com.goggles.user_service.user.application;

import com.goggles.common.exception.ForbiddenException;
import com.goggles.user_service.user.application.dto.UserServiceDto;
import com.goggles.user_service.user.domain.Role;
import com.goggles.user_service.user.domain.User;
import com.goggles.user_service.user.domain.UserRepository;
import com.goggles.user_service.user.domain.service.IdentityProvider;
import com.goggles.user_service.user.domain.service.MessageProvider;
import com.goggles.user_service.user.domain.service.RoleCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final IdentityProvider identityProvider;
    private final UserRepository userRepository;
    private final RoleCheck roleCheck;
    private final MessageProvider messageProvider;

    /**
     * 1. 외부 인증서버에서 계정 등록 (IdentityProvider::register()), 등록 성공시 UserId 반환
     * 2. 반환받은 UserId를 기본키(PK)로 하는 User 엔티티 생성 및 DB 저장
     * 3. DB 저장 실패시 외부 인증 서버에서 등록된 계쩡 롤백
     *
     */
    @Transactional
    public UUID signUp(UserServiceDto.SignUp data) {
        UUID userId = identityProvider.register(data.getEmail(), data.getPassword());

        try {

            User user = UserServiceDto.SignUp.toUser(userId, data);
            userRepository.save(user);

            return userId;
        } catch (Exception e) {
            // DB처리 실패 -> 외부 인증 서버에 등록 취소
            identityProvider.withdraw(userId);
            log.error("회원가입 처리 실패: {}", e.getMessage(), e);
            throw e;
        }
    }

    public void changePassword(UUID userId, String newPassword) {
        if (!roleCheck.hasRole(Role.MASTER) && !roleCheck.isMine()) {
            throw new ForbiddenException(messageProvider.getMessage("user.exception.mine.forbidden"));
        }

        identityProvider.changePassword(userId, newPassword);
    }
}
