package com.potato.exception;

public class UnAuthorizedException extends CustomException {

    public UnAuthorizedException(String message, Object data) {
        super(message, data);
    }

    public UnAuthorizedException(String message) {
        super(message);
    }

}
