package org.pgsg.chat.application;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.pgsg.chat.application.dto.CreateChatRoomCommand;
import org.pgsg.chat.application.service.ChatService;
import org.pgsg.chat.domain.model.ChatRoom;
import org.pgsg.chat.domain.model.RoomId;
import org.pgsg.chat.domain.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    void create_room_test(){
        // given
        UUID tradeId = UUID.randomUUID();
        CreateChatRoomCommand command = new CreateChatRoomCommand(
                tradeId,
                UUID.randomUUID(),
                "판매 상품1",
                UUID.randomUUID(),
                "판매자 1",
                UUID.randomUUID(),
                "구매자 1"
        );

        // when
        chatService.createRoom(command);
        ChatRoom chatRoom = chatRoomRepository.findById(RoomId.of(tradeId))
                .orElse(null);

        // then
        assertNotNull(chatRoom);
        assertEquals(tradeId, chatRoom.getId().getId());
        log.info("chatRoom={}", chatRoom);
    }
}
