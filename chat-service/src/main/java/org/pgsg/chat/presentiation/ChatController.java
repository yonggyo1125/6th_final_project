package org.pgsg.chat.presentiation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pgsg.chat.application.service.ChatService;
import org.pgsg.chat.presentiation.dto.ChatRelay;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/{roomId}/message")
    public void chatRelay(@DestinationVariable("roomId") UUID roomId, ChatRelay chatRelay) {
        log.info("메세지 전송: roomId: {}, chatRelay: {}", roomId, chatRelay);
        chatService.addMessage(roomId, chatRelay.type(), chatRelay.message());
        messagingTemplate.convertAndSend("/topic/room/"+ roomId,chatRelay);
    }
}
