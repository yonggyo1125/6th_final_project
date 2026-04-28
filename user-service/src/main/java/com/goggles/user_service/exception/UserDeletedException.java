package com.goggles.user_service.exception;

import com.goggles.common.exception.UnAuthorizedException;

public class UserDeletedException extends UnAuthorizedException {

    public UserDeletedException(){
        super("이미 탈퇴한 사용자 입니다.");
    }
}
