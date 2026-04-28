package com.goggles.user_service.user.domain.exception;

import com.goggles.common.exception.BadRequestException;

public class InvalidPasswordException extends BadRequestException {

    public InvalidPasswordException(String password){
        super("비밀번호가 잘못입력되었습니다. 비밀번호 =" + password);
    }
}
