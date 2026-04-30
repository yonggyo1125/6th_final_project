package org.pgsg.chat.infrastructure.persistence;

import org.pgsg.chat.domain.model.Room;
import org.pgsg.chat.domain.model.RoomId;
import org.pgsg.chat.domain.repository.ChatRoomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaChatRoomRepository extends ChatRoomRepository, JpaRepository<Room, RoomId> {

}
