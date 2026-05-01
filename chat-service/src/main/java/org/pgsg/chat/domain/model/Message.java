package org.pgsg.chat.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.pgsg.chat.domain.exception.ChatErrorCode;
import org.pgsg.chat.domain.exception.ChatServiceException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "p_chat_message")
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SenderType senderType;

    @Lob
    @Column(nullable = false)
    private String content;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public static Message of(SenderType type, String content) {
        if (type ==null) {
            throw new ChatServiceException(ChatErrorCode.CHAT_SENDER_INVALID_TYPE);
        }

        if (content == null || content.isBlank()) {
            throw new ChatServiceException(ChatErrorCode.CHAT_MESSAGE_EMPTY);
        }
        Message message = new Message();
        message.senderType = type;
        message.content = content;
        return message;
    }
}