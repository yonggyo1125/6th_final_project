package org.pgsg.chat.application.dto;

import org.pgsg.chat.domain.model.Room;

import java.util.UUID;

public record CreateChatRoomCommand(
        UUID tradeId,
        UUID productId,
        String productName,
        UUID sellerId,
        String sellerNickName,
        UUID buyerId,
        String buyerNickName
) {
    public Room toChatRoom() {
        return Room.builder()
                .tradeId(tradeId)
                .productId(productId)
                .productName(productName)
                .sellerId(sellerId)
                .sellerNickName(sellerNickName)
                .buyerId(buyerId)
                .buyerNickName(buyerNickName)
                .build();
    }
}
