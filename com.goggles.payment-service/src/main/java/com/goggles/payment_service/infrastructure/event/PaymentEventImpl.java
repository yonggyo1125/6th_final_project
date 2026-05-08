package com.goggles.payment_service.infrastructure.event;

import com.goggles.payment_service.domain.Payment;
import com.goggles.payment_service.domain.event.PaymentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventImpl implements PaymentEvent {

    @Override
    public void approved(Payment payment) {

    }

    @Override
    public void failed(Payment payment) {

    }

    @Override
    public void cancelled(Payment payment) {

    }

    @Override
    public void cancelFailed(Payment payment) {

    }
}
