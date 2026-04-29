package com.goggles.user_service.user.domain.exception;

import com.goggles.common.exception.ForbiddenException;

public class MasterOnlyException extends ForbiddenException {
    public MasterOnlyException(String message) {
        super(message);
    }
}
