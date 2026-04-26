package org.sparta.monitoringserver.prompts.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class CommonException extends RuntimeException {
    private final HttpStatus status;

    public CommonException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
