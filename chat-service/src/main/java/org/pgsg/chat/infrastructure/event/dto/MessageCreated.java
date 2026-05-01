package org.pgsg.chat.infrastructure.event.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.pgsg.chat.domain.model.SenderType;
import org.pgsg.common.domain.BaseEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public record MessageCreated(
        Long messageId,
        SenderType senderType,
        String message,
        LocalDateTime createdAt,

        @JsonIgnore
        String eventType,

        @JsonIgnore
        UUID correlationId
) implements BaseEvent {

    @Override
    public String eventType() {
        return eventType;
    }

    @Override
    public UUID domainId() {
        return UUID.randomUUID();
    }

    @Override
    public String domainType() {
        return "chat.message";
    }

    @Override
    public Object payload() {
        return this;
    }
}
