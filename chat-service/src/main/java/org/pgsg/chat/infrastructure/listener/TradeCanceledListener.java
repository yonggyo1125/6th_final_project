package org.pgsg.chat.infrastructure.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pgsg.chat.application.service.ChatService;
import org.pgsg.chat.infrastructure.listener.dto.TradeCanceled;
import org.pgsg.common.messaging.annotation.IdempotentConsumer;
import org.pgsg.common.util.JsonUtil;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TradeCanceledListener {

    private final ChatService chatService;

    @IdempotentConsumer("topics.trade.cancelled")
    @KafkaListener(topics = "${topics.trade.cancelled}", groupId="chat-service")
    public void onCanceled(Message<String> message, Acknowledgment ack) {
        TradeCanceled canceled = JsonUtil.fromJson(message.getPayload(), TradeCanceled.class);
        try {
            chatService.cancel(canceled.tradeId());
            ack.acknowledge();

            log.info("채팅 거래 취소 성공 - roomId: {}", canceled.tradeId());

        } catch (Exception e) {
            log.error("채팅 거래 취소 처리 실패: {}, roomId: {}", e.getMessage(), canceled.tradeId(), e);
            throw e;
        }
    }

    @KafkaListener(topics = "${topics.trade.cancelled}-dlt", groupId="chat-service")
    public void handleDLT(Message<String> message, Acknowledgment ack) {
        log.error("채팅 거래 취소 처리 최종 실패(DLT), 수동 처리 요망");
        // 수동 처리를 위한 DB 기록 필요!
    }
}
