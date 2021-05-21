package com.potato.common.exception.model;

import com.potato.common.exception.ErrorCode;

public class BadGatewayException extends CustomException {

    public BadGatewayException(String message) {
        super(message, ErrorCode.BAD_GATEWAY_EXCEPTION);
    }

}
