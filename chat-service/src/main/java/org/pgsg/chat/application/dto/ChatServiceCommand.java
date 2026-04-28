package org.pgsg.chat.application.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatServiceCommand {

    @Builder
    public static class CreateRoom {
        private UUID tradeId;
        private UUID productId;
        private String productName;
        private UUID sellerId;
        private String sellerNickName;
        private UUID buyerId;
        private String buyerNickName;
    }
}
