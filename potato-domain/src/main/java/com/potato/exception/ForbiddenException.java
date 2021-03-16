package com.potato.exception;

public class ForbiddenException extends CustomException {

    public ForbiddenException(String message, String description) {
        super(message, description);
    }

    public ForbiddenException(String message) {
        super(message);
    }

}
