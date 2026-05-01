package org.pgsg.chat.application.dto;

import lombok.Builder;
import org.pgsg.chat.domain.model.Room;

import java.util.UUID;

@Builder
public record RoomInfo(
        UUID roomId,
        UUID sellerId,
        String sellerNickname,
        UUID buyerId,
        String buyerNickname,
        UUID productId,
        String productName,
        String roomStatus
) {
    public static RoomInfo from(Room room) {
        return RoomInfo.builder()
                .roomId(room.getId().getId())
                .sellerId(room.getSeller().getId())
                .sellerNickname(room.getSeller().getNickname())
                .buyerId(room.getBuyer().getId())
                .buyerNickname(room.getBuyer().getNickname())
                .productId(room.getProduct().getId())
                .productName(room.getProduct().getName())
                .roomStatus(room.getStatus().name())
                .build();
    }
}
