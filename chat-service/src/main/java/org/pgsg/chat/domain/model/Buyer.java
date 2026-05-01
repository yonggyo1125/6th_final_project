package org.pgsg.chat.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.pgsg.chat.domain.exception.ChatErrorCode;
import org.pgsg.chat.domain.exception.ChatServiceException;
import org.springframework.util.StringUtils;

import java.util.UUID;

@ToString
@Embeddable
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Buyer {

    @Column(name = "buyer_member_id", nullable = false)
    private UUID id;

    @Column(name = "buyer_nickname", nullable = false)
    private String nickname;

    protected Buyer(UUID id, String nickname) {
        if (id == null) {
            throw new ChatServiceException(ChatErrorCode.CHAT_VALIDATION_BUYER_ID_REQUIRED);
        }

        if (!StringUtils.hasText(nickname)) {
            throw new ChatServiceException(ChatErrorCode.CHAT_VALIDATION_BUYER_NICKNAME_REQUIRED);
        }
        this.id = id;
        this.nickname = nickname;
    }
}
