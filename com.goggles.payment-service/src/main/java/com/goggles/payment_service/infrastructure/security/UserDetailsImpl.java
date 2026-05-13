package com.goggles.payment_service.infrastructure.security;

import com.goggles.payment_service.domain.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@ToString
public class UserDetailsImpl implements UserDetails {

    private final UUID userId;
    private final String name;
    private final String email;
    private final UserRole role;

    @Builder
    public UserDetailsImpl(UUID userId, String name, String email, UserRole role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role != null ?
                List.of(new SimpleGrantedAuthority("ROLE_" + role.name())) : List.of();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
