package org.pgsg.chat.application.service;

import lombok.RequiredArgsConstructor;
import org.pgsg.chat.application.dto.ChatServiceCommand;
import org.pgsg.chat.domain.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public UUID createRoom(ChatServiceCommand.CreateRoom createRoomDto) {

        return null;
    }

    // 채팅 방하나 조회


    // 채팅 방 목록 조회


    // 채팅 대화 기록

}
