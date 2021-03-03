package com.potato.exception;

import lombok.Getter;

@Getter
public abstract class CustomException extends RuntimeException {

    private Object data;

    public CustomException(String message, Object data) {
        super(message);
        this.data = data;
    }

    public CustomException(String message) {
        super(message);
    }

}
