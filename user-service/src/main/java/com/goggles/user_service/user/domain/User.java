package com.goggles.user_service.user.domain;

import com.goggles.common.domain.BaseTime;
import com.goggles.common.exception.BadRequestException;
import com.goggles.user_service.user.domain.exception.MasterOnlyException;
import com.goggles.user_service.user.domain.service.MessageProvider;
import com.goggles.user_service.user.domain.service.RoleCheck;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

/**
 * 1. 인증 처리는 키클록이 담당하므로 키클록이 발급한 UUID와 User의 기본키는 동일
 * 2. 인증을 키클록이 하므로 비밀번호는 정의하지 않는다.
 * 3. 가입은 14세 이상만 가능
 * 4. 강사 권한으로 변경은 마스터만 가능
 */
@Getter
@ToString
@Entity
@Table(name="P_USERS")
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTime {

    private final long MIN_AGE = 14; // 가입가능한 최소 연령

    @EmbeddedId
    private UserId id;

    // 기본정보
    @Column(length=45, nullable = false)
    private String name;

    @Column(length=45, nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(length=20, nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(length=15, nullable = false)
    private Gender gender;
    private LocalDate birthdate;

    // 연락처
    @Embedded
    private Contact contact;

    // 프로필
    @Embedded
    private Profile profile;

    // 약관 동의
    @Embedded
    private Consent consent;

    @Builder
    public User(
                UUID userId, String name, String nickname, Gender gender, LocalDate birthdate,  // 기본 정보
                String email, String phoneNumber, // 연락처
                List<Interest> interests, List<Job> jobs, List<String> educations, List<String> majors,  // 프로필
                boolean personalConsent, boolean marketingConsent, boolean emailConsent, // 약관 동의
                // 도메인 서비스
                MessageProvider messageProvider
    ) {

        this.id = UserId.of(userId);
        this.name = name;
        this.nickname = nickname;
        this.role = Role.STUDENT; // 기본값은 Student
        this.gender = gender;
        this.birthdate = birthdate;

        validateBasicInfo(messageProvider); // 기본정보 검증

        this.contact = new Contact(email, phoneNumber, messageProvider);
        this.profile = new Profile(interests, jobs, educations, majors, messageProvider);
        this.consent = new Consent(personalConsent, marketingConsent, emailConsent, messageProvider);
    }


    // 기본 회원정보 유효성 검사
    private void validateBasicInfo(MessageProvider messageProvider) {
        if (!StringUtils.hasText(this.name)) {
            throw new BadRequestException(messageProvider.getMessage("user.validation.name.required"));
        }

        if (!StringUtils.hasText(this.nickname)) {
            throw new BadRequestException(messageProvider.getMessage("user.validation.nickname.required"));
        }

        if (this.gender == null) {
            throw new BadRequestException(messageProvider.getMessage("user.validation.gender.required"));
        }

        if (this.birthdate == null) {
            throw new BadRequestException(messageProvider.getMessage("user.validation.birthdate.required"));
        }

        // 생년월일은 14세 이상만 가입 가능
        if (ChronoUnit.YEARS.between(this.birthdate, LocalDate.now()) < MIN_AGE) { // 최소 연령보다 작은 경우
            throw new BadRequestException(messageProvider.getMessage("user.validation.birthdate.invalid"));
        }
    }

    // 역할 변경은 마스터 관리자만 가능
    // 역할은 STUDENT, INSTRUCTOR 2가지 내에서만 가능
    public void changeRole(Role role, RoleCheck roleCheck, MessageProvider messageProvider) {
        if (this.role == role) { // 현재 Role과 동일하면 변경 필요 X
            return;
        }

        checkMasterOnly(roleCheck, messageProvider); // 권한 체크

        this.role = role;
    }

    // MASTER 권한인지 체크
    private void checkMasterOnly(RoleCheck roleCheck, MessageProvider messageProvider) {
        if (!roleCheck.hasRole(Role.MASTER)) {
            throw new MasterOnlyException(messageProvider.getMessage("user.exception.master.forbidden"));
        }
    }
}
