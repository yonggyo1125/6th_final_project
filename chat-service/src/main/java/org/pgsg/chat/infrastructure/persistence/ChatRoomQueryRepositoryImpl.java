package org.pgsg.chat.infrastructure.persistence;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.pgsg.chat.domain.model.ChatRoom;
import static org.pgsg.chat.domain.model.QChatRoom.chatRoom;

import org.pgsg.chat.domain.model.QChatRoom;
import org.pgsg.chat.domain.model.RoomId;
import org.pgsg.chat.domain.repository.ChatRoomQueryRepository;
import org.pgsg.chat.domain.repository.ChatRoomSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChatRoomQueryRepositoryImpl implements ChatRoomQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<ChatRoom> findById(RoomId roomId) {

        ChatRoom room = queryFactory.selectFrom(chatRoom)
                .where(
                        chatRoom.id.eq(roomId),
                        chatRoom.deletedAt.isNull()
                )
                .fetchOne();

        return Optional.ofNullable(room);
    }

    @Override
    public Page<ChatRoom> findAll(ChatRoomSearch search, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(chatRoom.deletedAt.isNull());

        if (search.roomIds() != null) { // 방 ID 여러개로 검색
            builder.and(chatRoom.id.in(search.roomIds()));
        }

        if (search.userIds() != null) {
            builder.and(
                    chatRoom.buyer.id.in(search.userIds())
                            .or(chatRoom.seller.id.in(search.userIds()))
            );
        }

        if (StringUtils.hasText(search.productName())) { // 상품명이 있을떄
            builder.and(chatRoom.product.name.contains(search.productName().trim()));
        }

        if (StringUtils.hasText(search.userName())) { // 사용자명
            builder.and(
                    chatRoom.buyer.nickname.contains(search.userName().trim())
                            .or(chatRoom.seller.nickname.contains(search.userName().trim()))
            );
        }

        if (StringUtils.hasText(search.keyword())) { // 키워드 조회 : 상품명 or 사용자명
            builder.and(
                    chatRoom.product.name.contains(search.keyword().trim())
                            .or(chatRoom.seller.nickname.contains(search.keyword().trim()))
                            .or(chatRoom.buyer.nickname.contains(search.keyword().trim()))
            );
        }

        List<ChatRoom> items = queryFactory
                .selectFrom(chatRoom)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(chatRoom.createdAt.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(chatRoom.count())
                .from(chatRoom)
                .where(builder);

        return PageableExecutionUtils.getPage(items, pageable, countQuery::fetchOne);
    }
}
