package com.goggles.payment_service.domain;

import com.goggles.payment_service.domain.exception.PaymentInvalidException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CancelDetail {
    @Column(name="cancel_reason", length=100)
    private String reason;
    private LocalDateTime canceledAt;

    protected CancelDetail(String reason) {
        if (!StringUtils.hasText(reason)) {
            throw new PaymentInvalidException("취소 사유는 필수 입력값입니다.");
        }

        this.reason = reason;
        this.canceledAt = LocalDateTime.now();
    }
}
