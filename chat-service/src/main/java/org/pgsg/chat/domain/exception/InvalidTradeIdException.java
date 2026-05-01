package org.pgsg.chat.domain.exception;

public class InvalidTradeIdException extends ChatServiceException {
    public InvalidTradeIdException() {
        super(ChatErrorCode.CHAT_VALIDATION_TRADE_ID_REQUIRED);
    }
}