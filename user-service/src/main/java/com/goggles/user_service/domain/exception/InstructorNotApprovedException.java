package com.goggles.user_service.domain.exception;

import com.goggles.common.exception.ForbiddenException;

public class InstructorNotApprovedException extends ForbiddenException {
    public InstructorNotApprovedException(String status) {
        super("승인되지 않은 강사입니다. status=" + status);
    }
}
