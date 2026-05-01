package org.pgsg.chat.domain.event;

import org.pgsg.chat.domain.model.Message;
import org.pgsg.chat.domain.model.Room;

public interface ChatEvents {
    void completed(Room room);
    void canceled(Room room);
    void roomCreated(Room room); // 방 생성시
    void messageSent(Message message); // 메세지 전송시
}
