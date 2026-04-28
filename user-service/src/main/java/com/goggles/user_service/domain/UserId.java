package com.goggles.user_service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
public class UserId {

    @Column(name="user_id", length=45)
    private UUID id;

    public static UserId of(UUID id) {
        return new UserId(id);
    }
}
