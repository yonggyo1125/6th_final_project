package com.goggles.user_service.user.domain.service;

import com.goggles.user_service.user.domain.UserId;

import java.util.UUID;

public interface IdentityProvider {
    // 원격 인증서버에서 가입 처리
    UUID register(String email, String password);

    // 원격 인증서버에서 등록 취소
    void withdraw(UUID id);

    // 비밀번호 변경
    void changePassword(UserId id, String newPassword);
}
