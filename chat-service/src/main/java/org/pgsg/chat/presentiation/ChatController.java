package org.pgsg.chat.presentiation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pgsg.chat.application.service.ChatService;
import org.pgsg.chat.presentiation.dto.ChatRelay;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    @MessageMapping("/{roomId}/message")
    @SendTo("/topic/room/{roomId}")
    public ChatRelay chatRelay(@DestinationVariable("roomId") UUID roomId, ChatRelay chatRelay) {
        log.info("메세지 전송: roomId: {}, chatRelay: {}", roomId, chatRelay);
        chatService.addMessage(roomId, chatRelay.type(), chatRelay.message());
        return chatRelay;
    }

    @PostMapping("/{roomId}/complete")
    public void completeChat(@PathVariable UUID roomId) {

        log.info("거래 완료 처리: {}", roomId);
    }

    @PostMapping("/{roomId}/cancel")
    public void cancelChat(@PathVariable UUID roomId) {

        log.info("거래 취소 처리: {}", roomId);
    }
}
