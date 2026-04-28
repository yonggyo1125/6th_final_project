package com.goggles.user_service.domain.entity;

import com.goggles.user_service.domain.exception.InvalidPasswordException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password {

    @Column(nullable = false)
    private String password;

    public static Password fromRaw(String rawPassword, PasswordEncoder encoder){
        validate(rawPassword);
        return new Password(encoder.encode(rawPassword));
    }

    private Password(String password){
        this.password = password;
    }

    private static void validate(String rawPassword) {
        if (rawPassword == null || rawPassword.length() < 10) {
            throw new InvalidPasswordException(rawPassword);
        }
    }
}
