package com.goggles.user_service.domain;

import com.goggles.common.domain.BaseTime;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

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
    @Column(length=15, nullable = false)
    private Gender gender;
    private LocalDate birthdate;

    // 연락처
    @Embedded
    private Contact contact;

    // 프로필


    // 약관 동의


}
