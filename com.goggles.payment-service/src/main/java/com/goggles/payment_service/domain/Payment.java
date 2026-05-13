package com.goggles.payment_service.domain;

import com.goggles.common.domain.BaseTime;
import com.goggles.payment_service.domain.event.PaymentEvent;
import com.goggles.payment_service.domain.event.dto.PaymentEventDto;
import com.goggles.payment_service.domain.exception.PaymentDuplicatedException;
import com.goggles.payment_service.domain.exception.PaymentInvalidException;
import com.goggles.payment_service.domain.service.*;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@ToString
@Table(name="p_payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseTime {

    @EmbeddedId
    private PaymentId paymentId;

    @Enumerated(EnumType.STRING)
    @Column(length=35, nullable=false)
    private PaymentStatus status; // 결제 상태


    @Embedded
    private OrderDetail orderDetail;

    @Embedded
    private PaymentDetail paymentDetail;

    @Embedded
    private CancelDetail cancelDetail;

    @JoinColumn(name="payment_id")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentLog> paymentLogs = new ArrayList<>();

    @Builder
    protected Payment(UUID orderId, String productName, long orderPrice, UUID customerId, String customerName, String customerEmail) {
        this.paymentId = PaymentId.of();
        this.orderDetail = new OrderDetail(orderId, productName, orderPrice, customerId, customerName, customerEmail);
        this.status = PaymentStatus.READY; // 결제 등록시 기본값 READY
    }

    // 결제 생성
    public static Payment create(UUID orderId, String productName, long orderPrice, UUID customerId, String customerName, String customerEmail, OrderChecker checker, PaymentEvent paymentEvent) {
        // 중복 등록인지 체크
        if (checker.isDuplicated(orderId)) {
            throw new PaymentDuplicatedException(orderId);
        }

        Payment payment = Payment.builder()
                .orderId(orderId)
                .productName(productName)
                .orderPrice(orderPrice)
                .customerId(customerId)
                .customerName(customerName)
                .customerEmail(customerEmail)
                .build();

        // 결제 등록 후 이벤트 발행
        paymentEvent.created(new PaymentEventDto.Created(
                orderId,
                LocalDateTime.now()
        ));

        return payment;
    }

    /**
     * 결제 승인 처리
     * 1. 외부 결제 승인 API에 요청을 보낸다( 예 - 토스: paymentKey, orderId, amount 값이 필수)
     * 2. 주문 정보의 결제 요청 금액과 실제로 결제된 금액이 다른 경우는 변조된 것으로 간주
     * 3. 결제금액이 변조가 되었으면 환불처리를 진행
     */
    public void approve(String paymentKey, ApprovePayment approvePayment, CancelPayment cancelPayment, PaymentEvent event) {
        // 이미 승인된 상태면 처리하지 않음
        if (this.status == PaymentStatus.DONE) {
            return;
        }

        // 승인 상태 전이가 가능한지 검증
        PaymentStatus.validateTransition(this.status, PaymentStatus.DONE);

        // paymentKey는 필수
        if (!StringUtils.hasText(paymentKey)) {
            String failReason = "Payment Key는 필수입력 값입니다.";
            event.approvalfailed(PaymentEventDto.ApprovalFailed.from(this, failReason));
            throw new PaymentInvalidException(failReason);
        }

        // 결제 승인 처리
        ApproveResult result = approvePayment.request(this.paymentId, paymentKey, this.orderDetail);
        if (!result.success()) {
            log(this.status, result.paymentLog());

            event.approvalfailed(PaymentEventDto.ApprovalFailed.from(this, result.reason()));

            throw new PaymentInvalidException(result.reason());
        }

        this.paymentDetail = new PaymentDetail(paymentKey, result.method(), result.paidAt());

        // 결제 요청 주문 금액과 실 승인 금액이 일치하지 않는다면 금액이 변조된 것으로 판단 -> 결제 취소
        long orderPrice = this.orderDetail.getOrderPrice(); // 주문 금액
        long approvedAmount = result.approvedAmount(); // 실 승인 금액
        if (orderPrice != approvedAmount) {
            String message = "실결제 금액과 최초 등록 금액과 불일치";
            cancelPayment.cancel(this.paymentId, paymentKey, message);

            log(PaymentStatus.ABORTED, result.paymentLog());
            this.status = PaymentStatus.ABORTED;

            event.approvalfailed(PaymentEventDto.ApprovalFailed.from(this, message));


            throw new PaymentInvalidException(message);
        }

        log(PaymentStatus.DONE, result.paymentLog());
        this.status = PaymentStatus.DONE;

        // 성공시
        event.approved(new PaymentEventDto.Approved(
                this.orderDetail.getOrderId(),
                this.paymentDetail.getPaymentKey(),
                this.orderDetail.getOrderPrice(),
                this.paymentDetail.getPaidAt()
        ));
    }

    /**
     * 결제 취소
     * 1. 입금이 확인된 승인 단계(DONE)에서만 가능
     * 2. 결제 취소 성공/실패시 이벤트 알림
     */
    public void cancel(String cancelReason, CancelPayment cancelPayment, PaymentEvent event) {
        // 이미 취소가 된 상태라면 진행하지 않음
        if (this.status == PaymentStatus.CANCELLED) {
            return;
        }

        // 결제 취소 상태로 전이 가능한지 검증
        PaymentStatus.validateTransition(this.status, PaymentStatus.CANCELLED);

        if (this.paymentDetail == null) {
            String failReason = "취소 처리를 위해 결제 정보는 필수입니다.";
            event.cancelFailed(PaymentEventDto.CancelFailed.from(this, failReason));

            throw new PaymentInvalidException(failReason);
        }

        String paymentKey = this.paymentDetail.getPaymentKey();
        if (!StringUtils.hasText(paymentKey)) {
            String failReason = "취소 처리를 위해 Payment Key는 필수입니다.";
            event.cancelFailed(PaymentEventDto.CancelFailed.from(this, failReason));
            throw new PaymentInvalidException(failReason);
        }

        if (!StringUtils.hasText(cancelReason)) {
            String failReason = "취소 사유는 필수 입력값입니다.";
            event.cancelFailed(PaymentEventDto.CancelFailed.from(this, failReason));

            throw new PaymentInvalidException(failReason);
        }

        // 결제 취소 요청
        CancelResult result = cancelPayment.cancel(this.paymentId, paymentKey, cancelReason);
        if (!result.success()) {
            log(this.status, result.paymentLog()); // 로그 기록
            event.cancelFailed(PaymentEventDto.CancelFailed.from(this, result.reason()));

            throw new PaymentInvalidException(result.reason());
        }

        // 성공시 처리
        this.cancelDetail = new CancelDetail(cancelReason);
        log(PaymentStatus.CANCELLED, result.paymentLog()); // 로그 기록
        this.status = PaymentStatus.CANCELLED; // 취소 상태 변경

        event.cancelled(new PaymentEventDto.Cancelled(
                this.orderDetail.getOrderId(),
                this.orderDetail.getOrderPrice(),
                LocalDateTime.now(),
                cancelReason
        ));
    }

    // 결제, 취소, 상태 변경 로그 기록
    private void log(PaymentStatus status, String log) {
        paymentLogs = paymentLogs == null ? new ArrayList<>() : paymentLogs;
        paymentLogs.add(new PaymentLog(this.status, status, log));
    }
}
