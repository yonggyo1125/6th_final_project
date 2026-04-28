package org.pgsg.chat.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.pgsg.chat.domain.service.ProductData;
import org.pgsg.chat.domain.service.ProductProvider;

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

    protected Product(UUID id, ProductProvider productProvider) {
        if (id == null) {
            throw new IllegalArgumentException("상품 ID는 필수입니다.");
        }

        if (productProvider == null) {
            throw new IllegalArgumentException("ProductProvider 주입되지 않았습니다.");
        }

        ProductData data = productProvider.getProductData(id);
        if (data == null || data.id() == null) {
            throw new IllegalArgumentException("상품을 찾을수 없습니다.");
        }

        this.id = id;
        this.name = data.name();
    }
}
