package com.goggles.payment_service.infrastructure.toss.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TossClientFallbackFactory implements FallbackFactory<TossClient> {

    @Override
    public TossClient create(Throwable cause) {
        return null;
    }
}
