package org.pgsg.chat.infrastructure.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pgsg.chat.application.service.ChatService;
import org.pgsg.common.messaging.annotation.IdempotentConsumer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TradeCanceledListener {

    private final ChatService chatService;

    @IdempotentConsumer("topics.trade.cancelled")
    @KafkaListener(topics = "${topics.trade.cancelled}", groupId="chat-service")
    public void onCanceled() {

    }
}
