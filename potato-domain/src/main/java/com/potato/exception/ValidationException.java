package com.potato.exception;

public class ValidationException extends CustomException {

    public ValidationException(String message, Object data) {
        super(message, data);
    }

    public ValidationException(String message) {
        super(message);
    }

}
