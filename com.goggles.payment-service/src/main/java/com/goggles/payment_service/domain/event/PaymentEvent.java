package com.goggles.payment_service.domain.event;

import com.goggles.payment_service.domain.event.dto.PaymentEventDto;

public interface PaymentEvent {
    void created(PaymentEventDto.Created event);
    void createFailed(PaymentEventDto.CreateFailed event  );
    void approved(PaymentEventDto.Approved event); // 결제 승인
    void approvalfailed(PaymentEventDto.ApprovalFailed event);
    void cancelled(PaymentEventDto.Cancelled event);
    void cancelFailed(PaymentEventDto.CancelFailed event);
}
