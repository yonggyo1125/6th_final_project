package com.goggles.user_service.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@ToString
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserId implements Serializable {

    @Column(name="user_id", length=45)
    private UUID id;

    public static UserId of(UUID id) {
        return new UserId(id);
    }
}
