package org.pgsg.chat.infrastructure.event;

import org.pgsg.chat.domain.event.ChatEvents;
import org.pgsg.chat.domain.model.ChatRoom;
import org.springframework.stereotype.Component;

@Component
public class ChatEventsImpl implements ChatEvents {
    @Override
    public void completed(ChatRoom room) {

    }

    @Override
    public void canceled(ChatRoom room) {

    }
}
