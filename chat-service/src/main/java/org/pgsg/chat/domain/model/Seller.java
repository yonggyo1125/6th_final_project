package org.pgsg.chat.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.pgsg.chat.domain.service.UserData;
import org.pgsg.chat.domain.service.UserProvider;

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

    protected Seller(UUID id, UserProvider userProvider) {
        if (id == null) {
            throw new IllegalArgumentException("판매자 ID는 필수입니다.");
        }

        if (userProvider == null) {
            throw new IllegalArgumentException("UserProvider가 주입되지 않았습니다.");
        }

        UserData data = userProvider.getUser(id);
        if (data == null || data.id() == null) {
            throw new IllegalArgumentException("판매자를 찾을수 없습니다.");
        }

        this.id = id;
        this.nickname = data.nickname();
    }
}
