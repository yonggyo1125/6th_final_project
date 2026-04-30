package org.pgsg.chat.infrastructure.listener.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TradeCreated(
    UUID tradeId,
    UUID productId,
    String productName,
    UUID sellerId,
    String sellerNickName,
    UUID buyerId,
    String buyerNickName
) {}
