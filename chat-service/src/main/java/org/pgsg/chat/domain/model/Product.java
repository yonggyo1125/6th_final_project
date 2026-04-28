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
public class Product {
    @Column(length = 45, name="product_id")
    private UUID id;

    @Column(length = 100, name="product_name")
    private String name;

    protected Product(UUID id, String name) {
        if (id == null) {
            throw new IllegalArgumentException("상품 ID는 필수입니다.");
        }

        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("상품명은 필수입니다.");
        }


        this.id = id;
        this.name = name;
    }
}
