package org.pgsg.chat.infrastructure.listener.dto;

import java.util.UUID;

public record TradeCanceled(
        UUID tradeId
) {}
