package com.goggles.payment_service.presentation;

import com.goggles.payment_service.application.query.PaymentQueryService;
import com.goggles.payment_service.presentation.dto.PaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentQueryService  paymentQueryService;

    @ResponseBody
    @GetMapping("/{orderId}/detail")
    public PaymentResponse.PaymentInfo getPayment(@PathVariable("orderId") UUID orderId) {
        return PaymentResponse.PaymentInfo.from(paymentQueryService.getPayment(orderId));
    }

    @GetMapping("/demo/{orderId}")
    public String paymentDemo() {

        return "demo/index";
    }
}
