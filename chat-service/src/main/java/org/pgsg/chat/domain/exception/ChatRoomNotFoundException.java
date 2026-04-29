package org.pgsg.chat.domain.exception;

public class ChatRoomNotFoundException extends ChatServiceException{
    public ChatRoomNotFoundException() {
        super("ChatRoomNotFoundException");
    }
}
