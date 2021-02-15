package com.potato.exception;

public class NotFoundException extends CustomException {

    public NotFoundException(String message, Object data) {
        super(message, data);
    }

    public NotFoundException(String message) {
        super(message);
    }

}
