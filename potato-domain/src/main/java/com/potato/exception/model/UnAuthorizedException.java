package com.potato.exception.model;

import com.potato.exception.ErrorCode;

public class UnAuthorizedException extends CustomException {

    public UnAuthorizedException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public UnAuthorizedException(String message) {
        super(message, ErrorCode.UNAUTHORIZED_EXCEPTION);
    }

}