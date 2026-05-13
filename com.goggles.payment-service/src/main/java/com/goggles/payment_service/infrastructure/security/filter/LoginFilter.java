package com.goggles.payment_service.infrastructure.security.filter;

import com.goggles.payment_service.domain.UserRole;
import com.goggles.payment_service.infrastructure.security.UserDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Component
public class LoginFilter extends OncePerRequestFilter {

    private static final String HEADER_USER_ID = "X-User-Id";
    private static final String HEADER_USER_ROLE = "X-User-Role";
    private static final String HEADER_USER_EMAIL = "X-User-Email";
    private static final String HEADER_USER_NAME = "X-User-Name";

    private final HandlerExceptionResolver resolver;

    public LoginFilter(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            doLogin(request);
        } catch (Exception e) {
            log.error("인증 실패 - 사유: {}", e.getMessage(), e);
            resolver.resolveException(request, response, null, e);
            return;
        }

        filterChain.doFilter(request, response);
    }

    // X-User-.. 요청 헤더의 정보를 바탕으로 로그인 처리
    private void doLogin(HttpServletRequest request) {
        // SecurityContext - 인증 정보 객체
        SecurityContextHolder.clearContext(); // 로그아웃

        String userId = request.getHeader(HEADER_USER_ID);
        if (!StringUtils.hasText(userId)) { // 미인증 상태
            return;
        }

        String userName =  request.getHeader(HEADER_USER_NAME);
        String email = request.getHeader(HEADER_USER_EMAIL);
        String role = request.getHeader(HEADER_USER_ROLE);

        try {
            if (StringUtils.hasText(userName)) {
                userName = URLDecoder.decode(userName, StandardCharsets.UTF_8);
            }

            UserRole userRole = StringUtils.hasText(role) ? UserRole.valueOf(role) : null;

            UserDetails userDetails = UserDetailsImpl.builder()
                    .userId(UUID.fromString(userId))
                    .name(userName)
                    .email(email)
                    .role(userRole)
                    .build();

            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); // 로그인 상태가 담겨있는 인증객체!
            SecurityContextHolder.getContext().setAuthentication(authentication); // 로그인 처리
        } catch (IllegalArgumentException e) {
            // 인코딩 변환 또는 UUID 변환시 실패한 경우 로그인 처리 하지 않음
            log.warn("Gateway 요청 헤더 데이터 변환 실패 - userId: {}, name: {}, email: {}, role: {}", userId, userName, email, role);
        }
    }
}
