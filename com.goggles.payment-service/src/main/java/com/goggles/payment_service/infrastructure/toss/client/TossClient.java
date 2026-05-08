package com.goggles.payment_service.infrastructure.toss.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(
        name = "toss-client",
        url = "https://api.tosspayments.com",
        fallbackFactory = TossClientFallbackFactory.class
)
public interface TossClient {
    @PostMapping(path="/v1/payments/confirm", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<JsonNode> approve(@RequestHeader("Idempotency-Key") String idempotenceKey, @RequestBody Map<String, Object> params);


    @PostMapping(path="/v1/payments/{paymentKey}/cance", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<JsonNode> cancel(@PathVariable("paymentKey") String paymentKey, @RequestHeader("Idempotency-Key") String idempotenceKey,@RequestBody Map<String, String> params);
}
