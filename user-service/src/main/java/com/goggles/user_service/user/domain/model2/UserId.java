package com.goggles.user_service.user.domain.model2;

import com.goggles.user_service.exception.InvalidUserIdException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserId {
    @Column(name = "user_id", nullable = false, updatable = false)
    private String userId;

    private UserId(String userId){
        validate(userId);
        this.userId = userId;
    }

    public static UserId of(UUID userId){
        if (userId == null) {
            throw new InvalidUserIdException(null);
        }
        return new UserId(userId.toString());
    }

    public static UserId generate(){
        return new UserId(UUID.randomUUID().toString());
    }

    private void validate(String userId){
        if(userId == null || userId.isBlank()){
            throw new InvalidUserIdException(userId);
        }
    }
}
