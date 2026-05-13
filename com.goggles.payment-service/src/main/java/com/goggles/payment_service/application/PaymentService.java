package com.goggles.payment_service.application;

import com.goggles.payment_service.application.dto.PaymentServiceDto;
import com.goggles.payment_service.domain.Payment;
import com.goggles.payment_service.domain.PaymentId;
import com.goggles.payment_service.domain.PaymentRepository;
import com.goggles.payment_service.domain.event.PaymentEvent;
import com.goggles.payment_service.domain.exception.PaymentNotFoundException;
import com.goggles.payment_service.domain.service.ApprovePayment;
import com.goggles.payment_service.domain.service.CancelPayment;
import com.goggles.payment_service.domain.service.OrderChecker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final ApprovePayment approvePayment;
    private final CancelPayment cancelPayment;
    private final PaymentEvent paymentEvent;
    private final OrderChecker orderChecker;

    @Transactional
    public UUID createPayment(PaymentServiceDto.Create dto) {
        log.info("결제 등록 시작 - 주문번호: {}, 상품명: {}, 주문금액: {}", dto.orderId(), dto.productName(), dto.orderPrice());

        Payment payment = Payment.create(
                dto.orderId(),
                dto.productName(),
                dto.orderPrice(),
                dto.customerId(),
                dto.customerName(),
                dto.customerEmail(),
                orderChecker,
                paymentEvent
        );


        paymentRepository.save(payment);

        UUID paymentId = payment.getPaymentId().getId();

        log.info("결제 등록 성공 - 결제 ID: {}", paymentId);

        return paymentId;
    }

    // 결제 승인
    @Transactional
    public void approvePayment(UUID paymentId, String paymentKey) {
        Payment payment = getPayment(paymentId);
        payment.approve(paymentKey, approvePayment, cancelPayment, paymentEvent);
    }

    // 결제 취소
    @Transactional
    public void cancelPayment(UUID paymentId, String cancelReason) {
        Payment payment = getPayment(paymentId);
        payment.cancel(cancelReason, cancelPayment, paymentEvent);
    }

    private Payment getPayment(UUID paymentId) {
        return paymentRepository.findById(PaymentId.of(paymentId))
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));
    }
}
