package com.goggles.payment_service.domain;

import com.goggles.common.domain.BaseTime;
import com.goggles.payment_service.domain.service.ApprovePayment;
import com.goggles.payment_service.domain.service.CancelPayment;
import jakarta.persistence.*;
import lombok.*;

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
    protected Payment(UUID orderId, String productName, long orderPrice) {
        this.paymentId = PaymentId.of();
        this.orderDetail = new OrderDetail(orderId, productName, orderPrice);
        this.status = PaymentStatus.READY; // 결제 등록시 기본값 READY
    }

    // 결제 생성
    public static Payment create(UUID orderId, String productName, long orderPrice) {
        return Payment.builder()
                .orderId(orderId)
                .productName(productName)
                .orderPrice(orderPrice)
                .build();
    }

    /**
     * 결제 승인 처리
     * 1. 외부 결제 승인 API에 요청을 보낸다( 예 - 토스: paymentKey, orderId, amount 값이 필수)
     * 2. 주문 정보의 결제 요청 금액과 실제로 결제된 금액이 다른 경우는 변조된 것으로 간주
     * 3. 결제금액이 변조가 되었으면 환불처리를 진행
     */
    public void approve(String paymentKey, ApprovePayment approvePayment, CancelPayment cancelPayment) {

    }

    // 결제 취소
    public void cancel(String cancelReason, CancelPayment cancelPayment) {

    }

    // 결제, 취소, 상태 변경 로그 기록
    private void log(PaymentStatus status, String log) {
        paymentLogs = paymentLogs == null ? new ArrayList<>() : paymentLogs;
        paymentLogs.add(new PaymentLog(this.status, status, log));
    }
}
