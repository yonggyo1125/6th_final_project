package org.pgsg.chat.domain.repository;

import lombok.Builder;
import org.pgsg.chat.domain.model.RoomId;

import java.util.List;
import java.util.UUID;

@Builder
public record ChatRoomSearch(
   List<RoomId> roomIds,
   List<UUID> userIds,
   String productName, // 상품명
   String userName,// 사용자명
   String keyword // 상품명 + 사용자명 키워드 조회
) {}
