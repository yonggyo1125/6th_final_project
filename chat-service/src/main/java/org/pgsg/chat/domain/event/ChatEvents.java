package org.pgsg.chat.domain.event;

import org.pgsg.chat.domain.model.Room;

public interface ChatEvents {
    void completed(Room room);
    void canceled(Room room);
}
