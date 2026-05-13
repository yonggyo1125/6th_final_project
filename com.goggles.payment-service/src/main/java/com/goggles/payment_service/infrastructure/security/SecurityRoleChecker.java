package com.goggles.payment_service.infrastructure.security;

import com.goggles.payment_service.domain.Payment;
import com.goggles.payment_service.domain.UserRole;
import com.goggles.payment_service.domain.service.RoleChecker;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityRoleChecker implements RoleChecker {

    @Override
    public boolean isMaster() {
        UserDetailsImpl user = getUserDetails();
        return user != null && user.getRole() == UserRole.MASTER;
    }

    @Override
    public boolean isMine(Payment payment) {
        UserDetailsImpl user = getUserDetails();

        return user != null && payment != null &&
                payment.getOrderDetail().getCustomerId().equals(user.getUserId());
    }

    private UserDetailsImpl getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {
            return userDetails;
        }

        return null;
    }
}
