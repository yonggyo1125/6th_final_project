package com.goggles.payment_service.domain;

import com.goggles.payment_service.domain.exception.PaymentInvalidException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class PaymentDetail {

    @Column(length = 45)
    private String transactionId;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private PaymentMethod method;

    private LocalDateTime paidAt;

    protected PaymentDetail(String transactionId, String method, LocalDateTime paidAt) {
        if (!StringUtils.hasText(transactionId)) {
            throw new PaymentInvalidException("Transaction ID는 필수 입력값입니다.");
        }

        if (!StringUtils.hasText(method)) {
            throw new PaymentInvalidException("결제 수단은 필수 입력값입니다.");
        }


        this.transactionId = transactionId;
        this.method = PaymentMethod.toEnum(method);
        this.paidAt = paidAt;
    }
}
