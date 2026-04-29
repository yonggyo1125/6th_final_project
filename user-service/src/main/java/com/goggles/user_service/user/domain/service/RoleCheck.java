package com.goggles.user_service.user.domain.service;

import com.goggles.user_service.user.domain.Role;

import java.util.List;

public interface RoleCheck {
    boolean hasRole(List<Role> roles);

    default boolean hasRole(Role role) {
        return hasRole(List.of(role));
    }

    boolean isMine(); // 로그인한 회원의 정보와 일치하는지
}
