package com.goggles.user_service.user.domain;

import com.goggles.common.domain.BaseTime;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * 1. 인증 처리는 키클록이 담당하므로 키클록이 발급한 UUID와 User의 기본키는 동일
 * 2. 인증을 키클록이 하므로 비밀번호는 정의하지 않는다.
 */
@Getter
@ToString
@Entity
@Table(name="P_USERS")
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTime {

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
                boolean personalConsent, boolean marketingConsent, boolean emailConsent // 약관 동의
    ) {

        this.id = UserId.of(userId);
        this.name = name;
        this.nickname = nickname;
        this.role = Role.STUDENT; // 기본값은 Student
        this.gender = gender;
        this.contact = new Contact(email, phoneNumber);
        this.profile = new Profile(interests, jobs, educations, majors);
        this.consent = new Consent(personalConsent, marketingConsent, emailConsent);

        setBirthdate(birthdate); // 생년월일


    }


    private void setBirthdate(LocalDate birthdate) {
        // 14세 이상만 가입 가능 체크
        this.birthdate = birthdate;
    }

}
