package org.pgsg.chat.application.service;

import lombok.RequiredArgsConstructor;
import org.pgsg.chat.application.dto.CreateChatRoomCommand;
import org.pgsg.chat.domain.exception.ChatRoomNotFoundException;
import org.pgsg.chat.domain.model.ChatRoom;
import org.pgsg.chat.domain.model.RoomId;
import org.pgsg.chat.domain.model.SenderType;
import org.pgsg.chat.domain.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public void createRoom(CreateChatRoomCommand dto) {
        chatRoomRepository.save(dto.toChatRoom());
    }


    // 채팅 대화 기록
    @Transactional
    public void addMessage(UUID roomId, String senderType, String message) {
        ChatRoom chatRoom = chatRoomRepository.findById(RoomId.of(roomId))
                .orElseThrow(ChatRoomNotFoundException::new);
        chatRoom.addMessage(SenderType.valueOf(senderType), message);
    }

}
