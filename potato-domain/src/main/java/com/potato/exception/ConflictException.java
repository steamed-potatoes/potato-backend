package com.potato.exception;


public class ConflictException extends CustomException {

    public ConflictException(String message, Object data) {
        super(message, data);
    }

    public ConflictException(String message) {
        super(message);
    }

}
