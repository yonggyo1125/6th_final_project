package org.pgsg.chat.domain.exception;

import org.pgsg.common.exception.CustomException;

public class ChatServiceException extends CustomException {

    public ChatServiceException(String errorName){
        this(errorName,null);
    }
    public ChatServiceException(String errorName, String field){
        super(errorName,field);
    }
}
