package com.goggles.user_service.domain.model2;

import com.goggles.common.domain.BaseTime;
import com.goggles.user_service.domain.exception.UserDeletedException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "P_USERS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTime {

    @EmbeddedId
    private UserId id;

    @Embedded
    private Email email;

    @Embedded
    private Password password;

    @Embedded
    private Name name;

    @Embedded
    private NickName nickName;

    @Embedded
    private PhoneNumber phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private Boolean personalInfoConsent;

    @Column(nullable = false)
    private Boolean marketingConsent;

    @Embedded
    private Experience experience;


    public static User create(
            Email email,
            Password password,
            Name name,
            NickName nickName,
            Gender gender,
            PhoneNumber phoneNumber,
            Role role,
            LocalDate birthDate,
            Boolean personalInfoConsent,
            Boolean marketingConsent,
            Experience experience
    ){
        User user = new User();
        user.id = UserId.generate();
        user.email = email;
        user.password = password;
        user.name = name;
        user.nickName = nickName;
        user.gender = gender;
        user.phoneNumber = phoneNumber;
        user.role = role;
        user.status = UserStatus.ACTIVE;
        user.birthDate = birthDate;
        user.personalInfoConsent = personalInfoConsent;
        user.marketingConsent = marketingConsent;
        user.experience = experience;
        return user;
    }

    public void userDelete() {
        if (this.status == UserStatus.DELETED) {
            throw new UserDeletedException();
        }
        this.status = UserStatus.DELETED;
    }
}
