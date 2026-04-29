package com.goggles.user_service.user.application;

import com.goggles.user_service.user.application.dto.UserServiceDto;
import com.goggles.user_service.user.domain.User;
import com.goggles.user_service.user.domain.UserId;
import com.goggles.user_service.user.domain.UserRepository;
import com.goggles.user_service.user.domain.service.IdentityProvider;
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
            User user = User.builder()
                    .userId(userId)
                    .email(data.getEmail())
                    .gender(data.getGender())
                    .name(data.getName())
                    .nickname(data.getNickname())
                    .birthdate(data.getBirthdate())
                    .phoneNumber(data.getPhoneNumber())
                    .interests(data.getInterests())
                    .jobs(data.getJobs())
                    .educations(data.getEducations())
                    .majors(data.getMajors())
                    .personalConsent(data.isPersonalConsent())
                    .marketingConsent(data.isMarketingConsent())
                    .emailConsent(data.isEmailConsent())
                    .build();

            userRepository.save(user);

            return userId;
        } catch (Exception e) {
            // DB처리 실패 -> 외부 인증 서버에 등록 취소
            identityProvider.withdraw(userId);
            log.error("회원가입 처리 실패: {}", e.getMessage(), e);
            throw e;
        }
    }
}
