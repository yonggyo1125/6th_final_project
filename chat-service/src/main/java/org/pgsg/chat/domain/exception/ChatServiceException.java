package org.pgsg.chat.domain.exception;

import org.pgsg.common.exception.CustomException;
import org.pgsg.common.exception.ErrorCode;

public class ChatServiceException extends CustomException {

    public ChatServiceException(ErrorCode errorCode){
        this(errorCode,null);
    }
    public ChatServiceException(ErrorCode errorCode, String field){
        super(errorCode,field);
    }
}
