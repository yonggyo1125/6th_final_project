package com.goggles.payment_service.application.query;


import com.goggles.payment_service.domain.exception.PaymentNotFoundException;
import com.goggles.payment_service.domain.query.PaymentQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentQueryService {
    private final PaymentQueryRepository queryRepository;

    public PaymentQueryResult getPayment(UUID orderId) {
        return queryRepository.findByOrderId(orderId)
                .map(PaymentQueryResult::from)
                .orElseThrow(() -> new PaymentNotFoundException("주문 ID: %s로 결제 정보를 조회할 수 없습니다.".formatted(orderId)));
    }
}
