package com.goggles.user_service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Contact {
    @Column(length=65, nullable = false, unique = true)
    private String email;

    @Column(length=20, nullable = false)
    private String phoneNumber;
}
