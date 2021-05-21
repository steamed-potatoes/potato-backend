package com.potato.common.exception.model;

import com.potato.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public abstract class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
