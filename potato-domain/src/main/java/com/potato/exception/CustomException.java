package com.potato.exception;

import com.potato.exception.ErrorCode;
import lombok.Getter;

@Getter
public abstract class CustomException extends RuntimeException {

    private ErrorCode errorCode;

    public CustomException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public CustomException(String message) {
        super(message);
    }

}
