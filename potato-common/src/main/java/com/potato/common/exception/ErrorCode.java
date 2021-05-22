package com.potato.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common
    VALIDATION_EXCEPTION(ErrorStatusCode.VALIDATION_EXCEPTION, "잘못된 입력입니다"),
    UNAUTHORIZED_EXCEPTION(ErrorStatusCode.UNAUTHORIZED_EXCEPTION, "세션이 만료되었습니다. 다시 로그인 해주세요"),
    FORBIDDEN_EXCEPTION(ErrorStatusCode.FORBIDDEN_EXCEPTION, "해당 권한이 없습니다"),
    NOT_FOUND_EXCEPTION(ErrorStatusCode.NOT_FOUND_EXCEPTION, "존재하지 않습니다"),
    CONFLICT_EXCEPTION(ErrorStatusCode.CONFLICT_EXCEPTION, "이미 존재하고 있습니다"),
    INTERNAL_SERVER_EXCEPTION(ErrorStatusCode.INTERNAL_SERVER_EXCEPTION, "서버 내부에서 에러가 발생하였습니다"),
    BAD_GATEWAY_EXCEPTION(ErrorStatusCode.BAD_GATEWAY_EXCEPTION, "외부 호출 중 에러가 발생하였습니다"),
    METHOD_NOT_ALLOWED_EXCEPTION(ErrorStatusCode.METHOD_NOT_ALLOWED_EXCEPTION, "허용되지 않은 HTTP 메소드 입니다"),

    // VALIDATION_EXCEPTION
    VALIDATION_CLASS_NUMBER_EXCEPTION(ErrorStatusCode.VALIDATION_EXCEPTION, "잘못된 학번입니다"),
    VALIDATION_SUBDOMAIN_EXCEPTION(ErrorStatusCode.VALIDATION_EXCEPTION, "허용되지 않은 도메인 입니다"),
    VALIDATION_FILE_FORMAT_EXCEPTION(ErrorStatusCode.VALIDATION_EXCEPTION, "허용되지 않은 파일입니다"),
    VALIDATION_GOOGLE_CODE_EXCEPTION(ErrorStatusCode.VALIDATION_EXCEPTION, "잘못된 구글 코드 입니다"),
    VALIDATION_DATETIME_INTERVAL_EXCEPTION(ErrorStatusCode.VALIDATION_EXCEPTION, "시작날짜가 종료날짜보다 빠를 수 없습니다"),
    VALIDATION_EMAIL_FORMAT_EXCEPTION(ErrorStatusCode.VALIDATION_EXCEPTION, "이메일 형식에 어긋납니다"),

    // FORBIDDEN_EXCEPTION
    FORBIDDEN_NOT_ORGANIZATION_ADMIN_EXCEPTION(ErrorStatusCode.FORBIDDEN_EXCEPTION, "동아리의 관리자만이 할 수 있습니다"),
    FORBIDDEN_NOT_ORGANIZATION_MEMBER_EXCEPTION(ErrorStatusCode.FORBIDDEN_EXCEPTION, "동아리의 소속만이 할 수 있습니다"),
    FORBIDDEN_COMMENT_DEPTH_EXCEPTION(ErrorStatusCode.VALIDATION_EXCEPTION, "댓글에 대댓글까지만 입력할 수 있습니다");


    private final ErrorStatusCode statusCode;
    private final String message;

    @Getter
    @RequiredArgsConstructor
    private enum ErrorStatusCode {
        VALIDATION_EXCEPTION(400, "VALIDATION_EXCEPTION"),
        UNAUTHORIZED_EXCEPTION(401, "UNAUTHORIZED_EXCEPTION"),
        FORBIDDEN_EXCEPTION(403, "FORBIDDEN_EXCEPTION"),
        NOT_FOUND_EXCEPTION(404, "NOT_FOUND_EXCEPTION"),
        METHOD_NOT_ALLOWED_EXCEPTION(405, "METHOD_NOT_ALLOWED_EXCEPTION"),
        CONFLICT_EXCEPTION(409, "CONFLICT_EXCEPTION"),
        INTERNAL_SERVER_EXCEPTION(500, "INTERNAL_SERVER_EXCEPTION"),
        BAD_GATEWAY_EXCEPTION(502, "BAD_GATEWAY_EXCEPTION");

        private final int statusCode;
        private final String code;
    }

    public String getCode() {
        return this.statusCode.getCode();
    }

}
