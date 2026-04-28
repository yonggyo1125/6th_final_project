package com.goggles.user_service.domain.model2;

import com.goggles.user_service.domain.exception.InvalidPhoneNumberException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhoneNumber {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^01\\d{8,9}$");

    @Column(name ="phone_number", nullable = false, unique = true)
    private String phoneNumber;

    private PhoneNumber(String phoneNumber){
        String normalized = normalize(phoneNumber);
        validate(normalized);
        this.phoneNumber = normalized;
    }

    public static PhoneNumber of(String phoneNumber){
        return new PhoneNumber(phoneNumber);
    }

    private static String normalize(String phoneNumber) {
        return phoneNumber == null ? null : phoneNumber.replace("-", "");
    }

    private static void validate(String phoneNumber) {
        if (phoneNumber == null || !PHONE_PATTERN.matcher(phoneNumber).matches()) {
            throw new InvalidPhoneNumberException(phoneNumber);
        }
    }
}
