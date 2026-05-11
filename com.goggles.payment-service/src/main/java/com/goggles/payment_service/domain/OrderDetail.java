package com.goggles.payment_service.domain;

import com.goggles.payment_service.domain.exception.PaymentInvalidException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDetail {
    @Column(length=45, nullable=false, unique=true)
    private UUID orderId;

    @Column(length=100, nullable=false)
    private String productName;

    private long orderPrice;

    /**
     * 1. 주문번호, 주문상품, 금액은 필수 입력 값
     *
     */
    protected OrderDetail(UUID orderId, String productName, long orderPrice) {
        if (orderId == null) {
            throw new PaymentInvalidException("주문 ID는 필수 입력값 입니다.");
        }

        if (!StringUtils.hasText(productName)) {
            throw new PaymentInvalidException("주문상품은 필수 입력값 입니다.");
        }

        if (orderPrice <= 0) {
            throw new PaymentInvalidException("결제금액이 유효하지 않습니다.");
        }

        this.orderId = orderId;
        this.productName = productName;
        this.orderPrice = orderPrice;
    }
}
