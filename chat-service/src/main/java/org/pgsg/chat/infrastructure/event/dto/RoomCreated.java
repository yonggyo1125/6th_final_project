package org.pgsg.chat.infrastructure.event.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.pgsg.chat.domain.model.RoomStatus;
import org.pgsg.common.domain.BaseEvent;

import java.util.UUID;

public record RoomCreated(
        UUID roomId,
        RoomStatus roomStatus,

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
    public UUID correlationId() {
        return correlationId;
    }

    @Override
    public UUID domainId() {
        return roomId;
    }

    @Override
    public String domainType() {
        return "chat.room";
    }

    @Override
    public Object payload() {
        return this;
    }
}
