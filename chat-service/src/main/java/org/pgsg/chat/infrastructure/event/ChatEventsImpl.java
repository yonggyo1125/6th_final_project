package org.pgsg.chat.infrastructure.event;

import org.pgsg.chat.domain.event.ChatEvents;
import org.pgsg.chat.domain.model.Room;
import org.springframework.stereotype.Component;

@Component
public class ChatEventsImpl implements ChatEvents {
    @Override
    public void completed(Room room) {
        
    }

    @Override
    public void canceled(Room room) {

    }
}
