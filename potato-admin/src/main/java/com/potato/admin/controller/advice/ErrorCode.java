package com.potato.admin.controller.advice;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    VALIDATION_EXCEPTION(400, "VALIDATION_EXCEPTION", "잘못된 입력입니다"),
    UNAUTHORIZED_EXCEPTION(401, "UNAUTHORIZED_EXCEPTION", "세션이 만료되었습니다. 다시 로그인 해주세요"),
    FORBIDDEN_EXCEPTION(403, "FORBIDDEN_EXCEPTION", "해당 권한이 없습니다"),
    NOT_FOUND_EXCEPTION(404, "NOT_FOUND_EXCEPTION", "존재하지 않습니다"),
    CONFLICT_EXCEPTION(409, "CONFLICT_EXCEPTION", "이미 존재하고 있습니다"),
    INTERNAL_SERVER_EXCEPTION(500, "INTERNAL_SERVER_EXCEPTION", "서버 내부에서 에러가 발생하였습니다"),
    BAD_GATEWAY_EXCEPTION(502, "BAD_GATEWAY_EXCEPTION", "외부 호출 중 에러가 발생하였습니다");

    private final int statusCode;
    private final String code;
    private final String message;

}
