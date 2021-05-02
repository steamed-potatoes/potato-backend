package com.potato.exception.model;


import com.potato.exception.ErrorCode;

public class ConflictException extends CustomException {

    public ConflictException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public ConflictException(String message) {
        super(message, ErrorCode.CONFLICT_EXCEPTION);
    }

}
