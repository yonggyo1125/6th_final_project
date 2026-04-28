package com.goggles.user_service.exception;

import com.goggles.common.exception.ConflictException;

public class DuplicateNicknameException extends ConflictException {

    public DuplicateNicknameException(String nickname){
        super("이미 사용중인 닉네임입니다. nickname=" + nickname);
    }
}
