package com.goggles.user_service.domain.entity;

import com.goggles.user_service.domain.exception.InvalidNameException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Name {

    @Column(nullable = false)
    private String name;

    private Name(String name){
        validate(name);
        this.name = name;
    }

    public static Name of(String name){
        return new Name(name);
    }

    private void validate(String name){
        if (name == null || name.isBlank()) {
            throw new InvalidNameException(name);
        }

        if (name.length() < 2 || name.length() > 20) {
            throw new InvalidNameException(name);
        }

        if (!name.matches("^[a-zA-Z가-힣]+$")) {
            throw new InvalidNameException(name);
        }
    }
}