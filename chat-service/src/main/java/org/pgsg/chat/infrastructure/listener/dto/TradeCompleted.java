package org.pgsg.chat.infrastructure.listener.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TradeCompleted(
        UUID tradeId
) {}
