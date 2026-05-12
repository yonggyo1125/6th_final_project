package com.goggles.payment_service.presentation;

import com.goggles.payment_service.application.PaymentService;
import com.goggles.payment_service.application.query.PaymentQueryResult;
import com.goggles.payment_service.application.query.PaymentQueryService;
import com.goggles.payment_service.presentation.dto.PaymentRequest;
import com.goggles.payment_service.presentation.dto.PaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentQueryService  paymentQueryService;
    private final PaymentService paymentService;

    @GetMapping("/{orderId}/detail")
    public PaymentResponse.PaymentInfo getPayment(@PathVariable("orderId") UUID orderId) {
        return PaymentResponse.PaymentInfo.from(paymentQueryService.getPayment(orderId));
    }

    @GetMapping("/success")
    public PaymentResponse.PaymentApprove approvePayment(PaymentRequest.Success success) {
        PaymentQueryResult result = paymentQueryService.getPayment(success.orderId());
        paymentService.approvePayment(result.paymentId(), result.paymentKey());

        return new PaymentResponse.PaymentApprove(result.paymentId());
    }

    @GetMapping("/{orderId}/cancel")
    public PaymentResponse.PaymentCancel cancelPayment(@PathVariable("orderId") UUID orderId) {
        PaymentQueryResult result = paymentQueryService.getPayment(orderId);
        paymentService.cancelPayment(result.paymentId(), result.paymentKey());

        return new PaymentResponse.PaymentCancel(result.paymentId());
    }
}
