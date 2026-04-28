package org.pgsg.chat.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.pgsg.chat.domain.event.ChatEvents;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Table(name = "p_chat_room")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @EmbeddedId
    private RoomId id;

    @Embedded
    private Seller seller;

    @Embedded
    private Buyer buyer;

    @Embedded
    private Product product;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;


    @JoinColumn(name="chatroom_id")
    @OrderBy("createdAt ASC")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> messages = new ArrayList<>();

    @Builder
    public ChatRoom(UUID tradeId,UUID productId, String productName, UUID sellerId, String sellerNickName, UUID buyerId, String buyerNickName, RoomStatus status) {
        this.id = RoomId.of(tradeId);
        this.seller = new Seller(sellerId, sellerNickName);
        this.buyer = new Buyer(buyerId, buyerNickName);
        this.product = new Product(productId, productName);
        this.status = status;
    }

    // 채팅 메세지 등록
    public void addMessage(SenderType type, String content){
        this.messages.add(ChatMessage.of(type, content));
    }

    // 마지막 메세지 등록 일시
    public LocalDateTime getLastMessageAt() {
        return this.messages == null || this.messages.size() == 0 ? null : this.messages.getLast().getCreatedAt();
    }

    // 완료 상태 변경
    public void complete(ChatEvents events) {
        this.status = RoomStatus.COMPLETED;

        // 완료 상태 변경 후 후속 처리 알림
        events.completed(this);
    }

    public void cancel(ChatEvents events) {
        this.status = RoomStatus.CANCELED;

        // 취소 상태 변경 후 후속 처리 알림
        events.canceled(this);
    }
 }
