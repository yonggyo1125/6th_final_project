package com.goggles.user_service.user.domain.exception;

import com.goggles.common.exception.UnAuthorizedException;

public class UserNotActiveException extends UnAuthorizedException {

    public UserNotActiveException(String status) {
        super("비활성 상태의 계정입니다. status=" + status);
    }
}
