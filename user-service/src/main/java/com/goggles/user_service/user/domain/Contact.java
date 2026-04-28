package com.goggles.user_service.user.domain;

import com.goggles.common.exception.BadRequestException;
import com.goggles.user_service.user.domain.service.MessageProvider;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.StringUtils;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Contact {

    private final String EMAIL_PATTERN = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?<domain>[a-z0-9-]+(.[a-z0-9-]+)*(\\.[a-z]{2,3}))$";
    private final String PHONE_NUMBER_PATTERN = "^010\\D*\\d{4}\\D*\\d{4}$";

    @Column(length=65, nullable = false, unique = true)
    private String email;

    @Column(length=20, nullable = false)
    private String phoneNumber;

    protected Contact(String email, String phoneNumber, MessageProvider messageProvider) {
        // 이메일 필수 여부 체크
        if (!StringUtils.hasText(email)) {
            throw new BadRequestException(messageProvider.getMessage("user.validation.email.required"));
        }

        // 이메일 형식 체크
        if (!email.matches(EMAIL_PATTERN)) {
            throw new BadRequestException(messageProvider.getMessage("user.validation.email.invalid"));
        }

        // 휴대전화번호 필수 여부 체크
        if (!StringUtils.hasText(phoneNumber)) {
            throw new BadRequestException(messageProvider.getMessage("user.validation.phone.required"));
        }

        // 휴대전화번호 형식 체크
        if (!phoneNumber.matches(PHONE_NUMBER_PATTERN)) {
            throw new BadRequestException(messageProvider.getMessage("user.validation.phone.invalid"));
        }


        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
