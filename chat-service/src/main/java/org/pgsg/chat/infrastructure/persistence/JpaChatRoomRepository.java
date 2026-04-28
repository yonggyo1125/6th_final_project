package org.pgsg.chat.infrastructure.persistence;

import org.pgsg.chat.domain.model.ChatRoom;
import org.pgsg.chat.domain.model.RoomId;
import org.pgsg.chat.domain.repository.ChatRoomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaChatRoomRepository extends ChatRoomRepository, JpaRepository<ChatRoom, RoomId> {

}
