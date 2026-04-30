package org.pgsg.chat.infrastructure.persistence;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.pgsg.chat.domain.model.Room;
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

import static org.pgsg.chat.domain.model.QRoom.room;

@Repository
@RequiredArgsConstructor
public class ChatRoomQueryRepositoryImpl implements ChatRoomQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Room> findById(RoomId roomId) {

        Room _room = queryFactory.selectFrom(room)
                .where(
                        room.id.eq(roomId),
                        room.deletedAt.isNull()
                )
                .fetchOne();

        return Optional.ofNullable(_room);
    }

    @Override
    public Page<Room> findAll(ChatRoomSearch search, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(room.deletedAt.isNull());

        if (search.roomIds() != null) { // 방 ID 여러개로 검색
            builder.and(room.id.in(search.roomIds()));
        }

        if (search.userIds() != null) {
            builder.and(
                    room.buyer.id.in(search.userIds())
                            .or(room.seller.id.in(search.userIds()))
            );
        }

        if (StringUtils.hasText(search.productName())) { // 상품명이 있을떄
            builder.and(room.product.name.contains(search.productName().trim()));
        }

        if (StringUtils.hasText(search.userName())) { // 사용자명
            builder.and(
                    room.buyer.nickname.contains(search.userName().trim())
                            .or(room.seller.nickname.contains(search.userName().trim()))
            );
        }

        if (StringUtils.hasText(search.keyword())) { // 키워드 조회 : 상품명 or 사용자명
            builder.and(
                    room.product.name.contains(search.keyword().trim())
                            .or(room.seller.nickname.contains(search.keyword().trim()))
                            .or(room.buyer.nickname.contains(search.keyword().trim()))
            );
        }

        List<Room> items = queryFactory
                .selectFrom(room)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(room.createdAt.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(room.count())
                .from(room)
                .where(builder);

        return PageableExecutionUtils.getPage(items, pageable, countQuery::fetchOne);
    }
}
