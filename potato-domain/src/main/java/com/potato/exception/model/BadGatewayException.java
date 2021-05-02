package com.potato.exception.model;

import com.potato.exception.ErrorCode;

public class BadGatewayException extends CustomException {

    public BadGatewayException(String message) {
        super(message, ErrorCode.BAD_GATEWAY_EXCEPTION);
    }

}
