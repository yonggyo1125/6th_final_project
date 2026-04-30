package org.pgsg.chat.domain.repository;

import org.pgsg.chat.domain.model.Room;
import org.pgsg.chat.domain.model.RoomId;

import java.util.Optional;

public interface ChatRoomRepository {
    Room save(Room room);
    Optional<Room> findById(RoomId id);
}
