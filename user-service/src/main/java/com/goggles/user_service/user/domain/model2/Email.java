package com.goggles.user_service.user.domain.model2;

import com.goggles.user_service.user.domain.exception.InvalidEmailException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email {
    @Column(nullable = false, unique = true)
    private String email;

    private Email(String email){
        validate(email);
        this.email = email;
    }

    public static Email of(String email){
        return new Email(email);
    }

    private void validate(String email){
        if(email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")){
            throw new InvalidEmailException(email);
        }
    }

}
