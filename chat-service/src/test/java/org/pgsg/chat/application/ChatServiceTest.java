package org.pgsg.chat.application;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.pgsg.chat.application.dto.ChatServiceCommand;
import org.pgsg.chat.application.service.ChatService;
import org.pgsg.chat.domain.model.ChatRoom;
import org.pgsg.chat.domain.model.RoomId;
import org.pgsg.chat.domain.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
public class ChatServiceTest {
    @Autowired
    ChatService chatService;

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Test
    @DisplayName("채팅방 생성 테스트")
    void create_room_test() {
        ChatServiceCommand.CreateRoom dto = ChatServiceCommand.CreateRoom
                .builder()
                .tradeId(UUID.randomUUID())
                .buyerId(UUID.randomUUID())
                .buyerNickName("구매자")
                .sellerId(UUID.randomUUID())
                .sellerNickName("판매자")
                .productId(UUID.randomUUID())
                .productName("판매상품")
                .build();

        chatService.createRoom(dto);

        ChatRoom chatRoom = chatRoomRepository.findById(RoomId.of(dto.getTradeId())).orElse(null);

        assertNotNull(chatRoom);
        assertEquals(dto.getTradeId(), chatRoom.getId().getId());

        log.info("chatRoom={}", chatRoom);

    }
}
