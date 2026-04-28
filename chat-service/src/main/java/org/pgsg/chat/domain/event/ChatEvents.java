package org.pgsg.chat.domain.event;

import org.pgsg.chat.domain.model.ChatRoom;

public interface ChatEvents {
    void completed(ChatRoom room);
    void canceled(ChatRoom room);
}
