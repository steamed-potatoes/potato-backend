package com.potato.common.exception.model;

import com.potato.common.exception.ErrorCode;

public class NotFoundException extends CustomException {

    public NotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public NotFoundException(String message) {
        super(message, ErrorCode.NOT_FOUND_EXCEPTION);
    }

}
