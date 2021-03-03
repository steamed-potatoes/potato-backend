package com.potato.exception;

public class BadGatewayException extends CustomException {

    public BadGatewayException(String message, Object data) {
        super(message, data);
    }

    public BadGatewayException(String message) {
        super(message);
    }

}
