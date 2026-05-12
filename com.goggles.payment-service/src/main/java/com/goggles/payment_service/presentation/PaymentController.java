package com.goggles.payment_service.presentation;

import com.goggles.payment_service.application.PaymentService;
import com.goggles.payment_service.application.query.PaymentQueryResult;
import com.goggles.payment_service.application.query.PaymentQueryService;
import com.goggles.payment_service.presentation.dto.PaymentRequest;
import com.goggles.payment_service.presentation.dto.PaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentQueryService  paymentQueryService;
    private final PaymentService paymentService;

    @ResponseBody
    @GetMapping("/{orderId}/detail")
    public PaymentResponse.PaymentInfo getPayment(@PathVariable("orderId") UUID orderId) {
        return PaymentResponse.PaymentInfo.from(paymentQueryService.getPayment(orderId));
    }

    @ResponseBody
    @GetMapping("/success")
    public PaymentResponse.PaymentApprove approvePayment(PaymentRequest.Success success) {
        PaymentQueryResult result = paymentQueryService.getPayment(success.orderId());
        paymentService.approvePayment(result.paymentId(), success.paymentKey());

        return new PaymentResponse.PaymentApprove(result.paymentId());
    }

    @ResponseBody
    @GetMapping("/{orderId}/cancel")
    public PaymentResponse.PaymentCancel cancelPayment(@PathVariable("orderId") UUID orderId) {
        PaymentQueryResult result = paymentQueryService.getPayment(orderId);
        paymentService.cancelPayment(result.paymentId(), result.paymentKey());

        return new PaymentResponse.PaymentCancel(result.paymentId());
    }

    @GetMapping("/failure")
    public String paymentFailure(@ModelAttribute("data") PaymentRequest.Failure failure) {
        return "demo/failure";
    }

    @GetMapping("/demo/{orderId}")
    public String paymentDemo(@PathVariable("orderId") UUID orderId, Model model) {
        PaymentQueryResult result = paymentQueryService.getPayment(orderId);
        log.info("result: {}", result);
        model.addAllAttributes(
                Map.of(
                        "orderId", result.orderId(),
                        "amount", result.amount(),
                        "orderName", result.orderName()
                )
        );
        return "demo/main";
    }
}
