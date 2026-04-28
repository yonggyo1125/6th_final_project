package com.goggles.user_service.domain.exception;

import com.goggles.common.exception.ConflictException;

public class InstructorAlreadyExistsException extends ConflictException {

    public InstructorAlreadyExistsException(String instructorId){
        super("이미 신청된 강사입니다. instructorId =" + instructorId);
    }
}
