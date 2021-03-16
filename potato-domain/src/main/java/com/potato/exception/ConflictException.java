package com.potato.exception;


public class ConflictException extends CustomException {

    public ConflictException(String message, String description) {
        super(message, description);
    }

    public ConflictException(String message) {
        super(message);
    }

}
