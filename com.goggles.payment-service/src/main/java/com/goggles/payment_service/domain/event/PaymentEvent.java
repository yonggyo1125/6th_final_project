package com.goggles.payment_service.domain.event;

import com.goggles.payment_service.domain.Payment;

public interface PaymentEvent {
    void approved(Payment payment); // 결제 승인
    void failed(Payment payment);
    void cancelled(Payment payment);
    void cancelFailed(Payment payment);
}
