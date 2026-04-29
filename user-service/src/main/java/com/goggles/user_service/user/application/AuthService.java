package com.goggles.user_service.user.application;

public interface AuthService {
    void getToken(String email, String password);
    void refreshToken(String refreshToken);
    void logout(String refreshToken);
}
