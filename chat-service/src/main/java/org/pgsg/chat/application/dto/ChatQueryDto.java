package org.pgsg.chat.application.dto;

import lombok.Builder;
import org.pgsg.chat.domain.model.RoomId;
import org.pgsg.chat.domain.repository.ChatRoomSearch;

import java.util.List;
import java.util.UUID;

// Search, Query, Find
@Builder
public record ChatQueryDto(
        List<RoomId> roomIds,
        List<UUID> userIds,
        String productName, // 상품명
        String userName,// 사용자명
        String keyword // 상품
) {
    public static ChatRoomSearch toSearch(ChatQueryDto dto) {
        return ChatRoomSearch.builder()
                .roomIds(dto.roomIds)
                .userIds(dto.userIds)
                .productName(dto.productName)
                .userName(dto.userName)
                .keyword(dto.keyword)
                .build();
    }

}
