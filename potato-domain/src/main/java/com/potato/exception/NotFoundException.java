package com.potato.exception;

public class NotFoundException extends CustomException {

    public NotFoundException(String message, String description) {
        super(message, description);
    }

    public NotFoundException(String message) {
        super(message);
    }

}
