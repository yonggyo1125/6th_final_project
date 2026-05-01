package org.pgsg.chat.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.pgsg.common.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum ChatErrorCode implements ErrorCode {
    CHAT_ROOM_NOT_FOUND("chat.room.not-found"),
    CHAT_ROOM_ALREADY_EXISTS("chat.room.already-exists"),
    CHAT_ROOM_INVALID_STATUS_TRANSITION("chat.room.invalid-status-transition"),

    CHAT_VALIDATION_SELLER_ID_REQUIRED("chat.validation.seller-id.required"),
    CHAT_VALIDATION_SELLER_ID_INVALID("chat.validation.seller-id.invalid"),
    CHAT_VALIDATION_BUYER_ID_REQUIRED("chat.validation.buyer-id.required"),
    CHAT_VALIDATION_SELLER_NICKNAME_REQUIRED("chat.validation.seller-nickname.required"),
    CHAT_VALIDATION_BUYER_NICKNAME_REQUIRED("chat.validation.buyer-nickname.required"),

    CHAT_VALIDATION_PRODUCT_ID_REQUIRED("chat.validation.product-id.required"),
    CHAT_VALIDATION_PRODUCT_NAME_REQUIRED("chat.validation.product-name.required"),
    CHAT_VALIDATION_TRADE_ID_REQUIRED("chat.validation.trade-id.required"),
    CHAT_STATUS_ALREADY_COMPLETED("chat.status.already-completed"),
    CHAT_STATUS_ALREADY_CANCELED("chat.status.already-cancelled"),
    CHAT_MESSAGE_EMPTY("chat.message.empty"),
    CHAT_SENDER_INVALID_TYPE("chat.sender.invalid-type");


    private final String errorKey;

}
