package com.potato.exception;

public class ValidationException extends CustomException {

    public ValidationException(String message, String description) {
        super(message, description);
    }

    public ValidationException(String message) {
        super(message);
    }

}
