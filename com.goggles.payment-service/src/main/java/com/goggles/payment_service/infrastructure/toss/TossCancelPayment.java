package com.goggles.payment_service.infrastructure.toss;

import com.goggles.payment_service.domain.PaymentId;
import com.goggles.payment_service.domain.service.CancelPayment;
import com.goggles.payment_service.domain.service.CancelResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TossCancelPayment implements CancelPayment {
    @Override
    public CancelResult cancel(PaymentId paymentId, String paymentKey, String cancelReason) {
        return null;
    }
}
