package org.pgsg.chat.infrastructure.event;

import lombok.RequiredArgsConstructor;
import org.pgsg.chat.domain.event.ChatEvents;
import org.pgsg.chat.domain.model.Message;
import org.pgsg.chat.domain.model.Room;
import org.pgsg.chat.infrastructure.event.dto.MessageCreated;
import org.pgsg.chat.infrastructure.event.dto.RoomCreated;
import org.pgsg.common.event.Events;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(ChatTopics.class)
public class ChatEventsImpl implements ChatEvents {

    private final ChatTopics topics;

    @Override
    public void completed(Room room) {
        
    }

    @Override
    public void canceled(Room room) {

    }

    @Override
    public void roomCreated(Room room) {
        Events.trigger(new RoomCreated(
                room.getId().getId(),
                room.getStatus(),
                topics.roomCreated(),
                UUID.randomUUID()
        ));
    }

    @Override
    public void messageSent(Message message) {
        Events.trigger(
                new MessageCreated(
                        message.getId(),
                        message.getSenderType(),
                        message.getContent(),
                        LocalDateTime.now(),
                        topics.messageSent(),
                        UUID.randomUUID()
                )
        );
    }
}
