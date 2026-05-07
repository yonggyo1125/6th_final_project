package com.goggles.payment_service;

import com.goggles.config.event.EventConfig;
import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.context.SecurityContextHolder;

@Import(EventConfig.class)
@SpringBootApplication(scanBasePackages = {"com.goggles"})
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = {"com.goggles"})
@EntityScan(basePackages = {"com.goggles"})
@EnableDiscoveryClient
@EnableConfigurationProperties
public class PaymentServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(PaymentServiceApplication.class, args);
  }

  @Bean
  public AuditorAware<UUID> auditorAware() {
    return () -> {
      try {
        return Optional.ofNullable(SecurityContextHolder.getContext())
            .map(ctx -> UUID.fromString(ctx.getAuthentication().getName()));
      } catch (Exception e) {
        return Optional.empty();
      }
    };
  }
}
