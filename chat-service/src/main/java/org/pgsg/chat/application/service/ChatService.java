package org.pgsg.chat.application.service;

import lombok.RequiredArgsConstructor;
import org.pgsg.chat.application.dto.ChatServiceCommand;
import org.pgsg.chat.domain.model.ChatRoom;
import org.pgsg.chat.domain.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public void createRoom(ChatServiceCommand.CreateRoom dto) {

        ChatRoom chatRoom = ChatRoom.builder()
                .tradeId(dto.getTradeId())
                .sellerId(dto.getSellerId())
                .sellerNickName(dto.getSellerNickName())
                .buyerId(dto.getBuyerId())
                .buyerNickName(dto.getBuyerNickName())
                .productId(dto.getProductId())
                .productName(dto.getProductName())
                .build();

        chatRoomRepository.save(chatRoom);
    }

    // 채팅 방하나 조회


    // 채팅 방 목록 조회


    // 채팅 대화 기록

}
