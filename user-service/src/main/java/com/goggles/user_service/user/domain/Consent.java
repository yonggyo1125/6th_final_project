package com.goggles.user_service.user.domain;

import com.goggles.common.exception.BadRequestException;
import com.goggles.user_service.user.domain.service.MessageProvider;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Consent {
    @Column(name="personal_consent")
    private boolean personal; // 개인정보 사용 동의

    @Column(name="marketing_consent")
    private boolean marketing; // 마케팅 정보로 활용 동의

    @Column(name="email_consent")
    private boolean email; // 이메일 수신 동의

    protected Consent(boolean personal, boolean marketing, boolean email, MessageProvider messageProvider) {
        // 개인정보 수집 필수 체크
        if (!personal) {
            throw new BadRequestException(messageProvider.getMessage("user.validation.personal.required"));
        }

        this.personal = personal;
        this.marketing = marketing;
        this.email = email;
    }
}
