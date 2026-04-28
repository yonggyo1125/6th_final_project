package com.goggles.user_service.user.domain.exception;

import com.goggles.common.exception.NotFoundException;

public class InstructorNotFoundException extends NotFoundException {

    public InstructorNotFoundException(String instructorId){
        super("강사 정보가 없습니다. instructorId = " + instructorId);
    }
}
