package com.potato.controller.advice;

import com.potato.controller.ApiResponse;
import com.potato.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.potato.controller.advice.ErrorCode.*;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Object> handleValidationException(ValidationException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(VALIDATION_EXCEPTION.getCode(), e.getDescription() == null ? VALIDATION_EXCEPTION.getMessage() : e.getDescription());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String field = e.getBindingResult().getFieldError() == null ? "" : e.getBindingResult().getFieldError().getField();
        return ApiResponse.error(VALIDATION_EXCEPTION.getCode(), String.format("(%s) %s", field, e.getBindingResult().getFieldError().getDefaultMessage()));
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Object> handleBindException(BindException e) {
        log.error(e.getMessage(), e);
        String field = e.getBindingResult().getFieldError() == null ? "" : e.getBindingResult().getFieldError().getField();
        return ApiResponse.error(VALIDATION_EXCEPTION.getCode(), String.format("(%s) %s", field, e.getBindingResult().getFieldError().getDefaultMessage()));
    }

    @ExceptionHandler(UnAuthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<Object> handleUnauthorizedException(UnAuthorizedException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(UNAUTHORIZED_EXCEPTION.getCode(), UNAUTHORIZED_EXCEPTION.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<Object> handleForbiddenException(ForbiddenException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(FORBIDDEN_EXCEPTION.getCode(), e.getDescription() == null ? FORBIDDEN_EXCEPTION.getMessage() : e.getDescription());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Object> handleNotFoundException(NotFoundException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(NOT_FOUND_EXCEPTION.getCode(), e.getDescription() == null ? NOT_FOUND_EXCEPTION.getMessage() : e.getDescription());
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse<Object> handleConflictException(ConflictException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(CONFLICT_EXCEPTION.getCode(), e.getDescription() == null ? CONFLICT_EXCEPTION.getMessage() : e.getDescription());
    }

    @ExceptionHandler(BadGatewayException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ApiResponse<Object> handleBadGatewayException(BadGatewayException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(BAD_GATEWAY_EXCEPTION.getCode(), BAD_GATEWAY_EXCEPTION.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Object> handleInternalServerException(IllegalArgumentException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(INTERNAL_SERVER_EXCEPTION.getCode(), INTERNAL_SERVER_EXCEPTION.getMessage());
    }

}
