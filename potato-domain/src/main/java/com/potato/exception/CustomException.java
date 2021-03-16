package com.potato.exception;

import lombok.Getter;

@Getter
public abstract class CustomException extends RuntimeException {

    private String description;

    public CustomException(String message, String description) {
        super(message);
        this.description = description;
    }

    public CustomException(String message) {
        super(message);
    }

}
