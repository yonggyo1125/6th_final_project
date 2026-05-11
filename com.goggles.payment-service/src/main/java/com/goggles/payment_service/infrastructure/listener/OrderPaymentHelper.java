package com.goggles.payment_service.infrastructure.listener;

import com.goggles.payment_service.domain.Payment;
import com.goggles.payment_service.domain.exception.PaymentNotFoundException;
import com.goggles.payment_service.domain.query.PaymentQueryRepository;

import java.util.UUID;

public class OrderPaymentHelper {

    public static Payment getPayment(UUID orderId, PaymentQueryRepository queryRepository) {
        return queryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new PaymentNotFoundException("주문 ID(%s)로 조회된 결제 정보가 없습니다.".formatted(orderId)));
    }
}
