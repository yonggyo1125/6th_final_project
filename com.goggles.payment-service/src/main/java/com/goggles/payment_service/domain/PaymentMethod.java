package com.goggles.payment_service.domain;

import com.goggles.payment_service.domain.exception.PaymentInvalidException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

// 카드, 가상계좌, 간편결제, 휴대폰, 계좌이체, 문화상품권, 도서문화상품권, 게임문화상품권
@Getter
@RequiredArgsConstructor
public enum PaymentMethod {
    CARD("카드"),
    VIRTUAL_ACCOUNT("가상계좌"),
    EASY_PAY("간편결제"),
    MOBILE_PAY("휴대폰"),
    LOCAL_BANK_TRANSFER("계좌이체"),
    CULTURE_VOUCHER("문화상품권"),
    BOOK_VOUCHER("도서문화상품권"),
    GAME_VOUCHER("게임문화상품권");

    private final String description;

    public static PaymentMethod toEnum(String paymentMethod) {
        // 가능한 문구 추출
        List<PaymentMethod> methods = Arrays.stream(PaymentMethod.values())
               .filter(m -> m.getDescription().equals(paymentMethod)).toList();

        if (methods.isEmpty()) { // 문자열의 결제 수단이 상수에 등록된 수단과 일치하지 않는 경우
            throw new PaymentInvalidException("처리할 수 없는 결제수단 입니다. - " + paymentMethod);
        }

        return methods.getFirst();
    }
}
