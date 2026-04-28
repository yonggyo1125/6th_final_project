package org.pgsg.chat.domain.service;

import java.util.UUID;

public interface ProductProvider {
    ProductData getProductData(UUID productId);
}
