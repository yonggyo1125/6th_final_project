package org.pgsg.chat.application.dto;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatServiceCommand {

    @Getter
    @Builder
    @ToString
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
