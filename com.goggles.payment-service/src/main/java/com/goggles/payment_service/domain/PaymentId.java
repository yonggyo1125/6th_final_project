package com.goggles.payment_service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.UUID;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentId {

    @Column(name="payment_id", length=45)
    private UUID id;

    public static PaymentId of(UUID id) {
        return new PaymentId(id);
    }

    public static PaymentId of() {
        return of(UUID.randomUUID());
    }
}
