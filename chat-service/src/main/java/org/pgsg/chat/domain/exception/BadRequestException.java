package org.pgsg.chat.domain.exception;

import org.pgsg.common.exception.CustomException;

public class BadRequestException extends CustomException {

    public BadRequestException(String errorName) {
        this(errorName, null);
    }
    public BadRequestException(String errorName, String field) {
        super(errorName, field);
    }
}
