package org.pgsg.chat.domain.repository;

import org.pgsg.chat.domain.model.ChatRoom;
import org.pgsg.chat.domain.model.RoomId;

import java.util.Optional;

public interface ChatRoomRepository {
    ChatRoom save(ChatRoom room);
    Optional<ChatRoom> findById(RoomId id);
}
