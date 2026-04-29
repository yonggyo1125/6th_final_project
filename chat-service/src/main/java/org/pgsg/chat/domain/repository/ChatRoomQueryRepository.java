package org.pgsg.chat.domain.repository;

import org.pgsg.chat.domain.model.ChatRoom;
import org.pgsg.chat.domain.model.RoomId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ChatRoomQueryRepository {
    Optional<ChatRoom> findById(RoomId roomId);
    Page<ChatRoom> findAll(ChatRoomSearch search, Pageable pageable);
}

