package com.potato.common.exception.model;

import com.potato.common.exception.ErrorCode;

public class ValidationException extends CustomException {

    public ValidationException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public ValidationException(String message) {
        super(message, ErrorCode.VALIDATION_EXCEPTION);
    }

}
