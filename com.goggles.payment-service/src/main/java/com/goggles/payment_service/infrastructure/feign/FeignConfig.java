package com.goggles.payment_service.infrastructure.feign;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@EnableFeignClients("com.goggles.payment_service")
public class FeignConfig {

    @Value("${toss.secret-key}")
    private String tossSecretKey;

    @Bean
    public RequestInterceptor requestInterceptor() {
        // Base64 인코딩 처리
        String encodedKey = Base64.getEncoder().encodeToString((tossSecretKey + ":").getBytes(StandardCharsets.UTF_8));

        return template -> {
            template.header(HttpHeaders.AUTHORIZATION, "Basic " + encodedKey);
        };
    }
}
