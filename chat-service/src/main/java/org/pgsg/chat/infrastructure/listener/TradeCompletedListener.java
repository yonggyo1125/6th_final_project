package org.pgsg.chat.infrastructure.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pgsg.chat.application.service.ChatService;
import org.pgsg.chat.infrastructure.listener.dto.TradeCompleted;
import org.pgsg.common.messaging.annotation.IdempotentConsumer;
import org.pgsg.common.util.JsonUtil;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TradeCompletedListener {
    private final ChatService chatService;

    @IdempotentConsumer("topics.trade.completed")
    @KafkaListener(topics = "${topics.trade.completed}", groupId="chat-service")
    public void onCompleted(Message<String> message, Acknowledgment ack) {
        TradeCompleted completed = JsonUtil.fromJson(message.getPayload(), TradeCompleted.class);
        try {
            chatService.complete(completed.tradeId());
            ack.acknowledge();

            log.info("채팅 거래 완료처리 - roomId: {}", completed.tradeId());
        } catch (Exception e) {
            log.error("채팅 거래 완료 처리 실패: {}, roomId: {}", e.getMessage(), completed.tradeId(), e);
            throw e;
        }
    }

    @KafkaListener(topics = "${topics.trade.completed}-dlt", groupId="chat-service")
    public void handleDLT(Message<String> message, Acknowledgment ack) {
        log.error("채팅 거래 완료 처리 최종 실패(DLT), 수동 처리 요망");
        // 수동 처리를 위한 DB 기록 필요!
    }
}
