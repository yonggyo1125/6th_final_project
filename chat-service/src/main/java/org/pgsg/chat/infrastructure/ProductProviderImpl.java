package org.pgsg.chat.infrastructure;

import org.pgsg.chat.domain.service.ProductData;
import org.pgsg.chat.domain.service.ProductProvider;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductProviderImpl implements ProductProvider {
    @Override
    public ProductData getProductData(UUID productId) {
        return null;
    }
}
