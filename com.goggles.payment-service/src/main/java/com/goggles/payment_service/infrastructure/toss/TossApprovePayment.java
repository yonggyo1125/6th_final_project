package com.goggles.payment_service.infrastructure.toss;

import com.goggles.payment_service.domain.OrderDetail;
import com.goggles.payment_service.domain.PaymentId;
import com.goggles.payment_service.domain.service.ApprovePayment;
import com.goggles.payment_service.domain.service.ApproveResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TossApprovePayment implements ApprovePayment {

    @Override
    public ApproveResult request(PaymentId paymentId, String paymentKey, OrderDetail orderDetail) {
        return null;
    }
}
