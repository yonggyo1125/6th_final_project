package org.pgsg.chat.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.util.StringUtils;

import java.util.UUID;

@ToString
@Embeddable
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Seller {

    @Column(name = "seller_member_id", nullable = false)
    private UUID id;

    @Column(name = "seller_nickname", nullable = false)
    private String nickname;

    protected Seller(UUID id, String nickname) {
        if (id == null) {
            throw new IllegalArgumentException("판매자 ID는 필수입니다.");
        }

        if (!StringUtils.hasText(nickname)) {
            throw new IllegalArgumentException("판매자 닉네임은 필수입니다.");
        }

        this.id = id;
        this.nickname = nickname;
    }
}
