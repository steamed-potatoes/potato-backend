package com.potato.exception;

public class ForbiddenException extends CustomException {

    public ForbiddenException(String message, Object data) {
        super(message, data);
    }

    public ForbiddenException(String message) {
        super(message);
    }

}
