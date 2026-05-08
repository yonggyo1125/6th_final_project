package com.goggles.payment_service.domain.service;

import com.goggles.payment_service.domain.OrderDetail;
import com.goggles.payment_service.domain.PaymentId;

public interface ApprovePayment {
    ApproveResult request(PaymentId paymentId, String paymentKey, OrderDetail orderDetail);
}
