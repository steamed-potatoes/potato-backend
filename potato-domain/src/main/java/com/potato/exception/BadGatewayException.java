package com.potato.exception;

public class BadGatewayException extends CustomException {

    public BadGatewayException(String message) {
        super(message, ErrorCode.BAD_GATEWAY_EXCEPTION);
    }

}
