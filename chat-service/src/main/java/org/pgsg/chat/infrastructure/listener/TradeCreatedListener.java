package org.pgsg.chat.infrastructure.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pgsg.chat.application.dto.CreateChatRoomCommand;
import org.pgsg.chat.application.service.ChatService;
import org.pgsg.common.messaging.annotation.IdempotentConsumer;
import org.pgsg.common.util.JsonUtil;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TradeCreatedListener {

    private final ChatService chatService;

    @Transactional
    @IdempotentConsumer("topic.trade.created")
    @KafkaListener(topics = "${topic.trade.created}", groupId="chat-service")
    public void onTradeCreated(Message<String> message, Acknowledgment ack) {
        try {
            TradeCreated created = JsonUtil.fromJson(message.getPayload(), TradeCreated.class);
            if (created != null) {
                chatService.createRoom(new CreateChatRoomCommand(
                        created.tradeId(),
                        created.productId(),
                        created.productName(),
                        created.sellerId(),
                        created.sellerNickname(),
                        created.buyerId(),
                        created.buyerNickname()
                ));
                ack.acknowledge(); // 커밋, 오프셋 기록
                log.info("채팅방 생성 성공: roomId: {}", created.tradeId());
            }
        } catch (Exception e) {
            log.error("채팅방 생성 실패: {}", e.getMessage(), e);
            throw e; // 재시도
        }
    }

    @KafkaListener(topics = "${topic.trade.created}.DLT", groupId="chat-service")
    public void handleDLT(Message<String> message, Acknowledgment ack) {
        log.error("채팅방 생성 최종 실패(DLT), 수동 처리 요망");
        // 수동 처리를 위한 DB 기록 필요!
    }
}
