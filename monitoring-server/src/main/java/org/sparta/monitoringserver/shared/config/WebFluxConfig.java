package org.sparta.monitoringserver.shared.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.ReactivePageableHandlerMethodArgumentResolver;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;

@Configuration
public class WebFluxConfig implements WebFluxConfigurer {
    @Override
    public void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {
        // Pageable 인터페이스를 PageRequest 구현체로 바인딩해주는 리졸버 등록
        configurer.addCustomResolver(new ReactivePageableHandlerMethodArgumentResolver());
    }
}
