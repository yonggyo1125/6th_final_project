package org.sparta.fileservice.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

// 구현 예정 - 공통 모듈에 있어야 함
@Configuration
public class SecurityConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return Optional::empty; // 임시
    }
}
