package com.goggles.payment_service.presentation;

import com.goggles.payment_service.application.query.PaymentQueryResult;
import com.goggles.payment_service.application.query.PaymentQueryService;
import com.goggles.payment_service.presentation.dto.PaymentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class DemoController {
    private final PaymentQueryService paymentQueryService;

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
        return "demo/index";
    }
}
