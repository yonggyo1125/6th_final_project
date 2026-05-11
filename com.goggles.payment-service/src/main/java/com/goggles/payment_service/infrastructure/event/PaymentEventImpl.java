package com.goggles.payment_service.infrastructure.event;

import com.goggles.common.event.Events;
import com.goggles.payment_service.domain.event.PaymentEvent;
import com.goggles.payment_service.domain.event.dto.PaymentEventDto;
import com.goggles.payment_service.infrastructure.topic.PaymentTopic;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(PaymentTopic.class)
public class PaymentEventImpl implements PaymentEvent {

    private final PaymentTopic topic;
    private final Events events;

    @Override
    public void created(PaymentEventDto.Created event) {
        events.trigger(event.orderId().toString(), "payment", topic.created(), event);
    }

    @Override
    public void createFailed(PaymentEventDto.CreateFailed event) {
        events.trigger(event.orderId().toString(), "payment", topic.createdFailed(), event);
    }

    @Override
    public void approved(PaymentEventDto.Approved event) {
        events.trigger(event.orderId().toString(), "payment", topic.confirmed(), event);
    }

    @Override
    public void approvalfailed(PaymentEventDto.ApprovalFailed event) {
        events.trigger(event.orderId().toString(), "payment", topic.confirmFailed(), event);
    }

    @Override
    public void cancelled(PaymentEventDto.Cancelled event) {
        events.trigger(event.orderId().toString(), "payment", topic.canceled(), event);
    }

    @Override
    public void cancelFailed(PaymentEventDto.CancelFailed event) {
        events.trigger(event.orderId().toString(), "payment", topic.cancelFailed(), event);
    }
}
