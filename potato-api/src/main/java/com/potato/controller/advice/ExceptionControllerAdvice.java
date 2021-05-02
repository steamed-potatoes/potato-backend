package com.potato.controller.advice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.potato.controller.ApiResponse;
import com.potato.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.potato.exception.ErrorCode.*;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ApiResponse<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String field = e.getBindingResult().getFieldError() == null ? "" : e.getBindingResult().getFieldError().getField();
        return ApiResponse.error(VALIDATION_EXCEPTION, String.format("%s - %s", field, e.getBindingResult().getFieldError().getDefaultMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    protected ApiResponse<Object> handleBadRequest(BindException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(VALIDATION_EXCEPTION);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidFormatException.class)
    protected ApiResponse<Object> handleMethodArgumentTypeMismatchException(InvalidFormatException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(VALIDATION_EXCEPTION);
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ApiResponse<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(METHOD_NOT_ALLOWED_EXCEPTION);
    }

    // Custom Exception
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Object> handleValidationException(ValidationException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(e.getErrorCode());
    }

    @ExceptionHandler(UnAuthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<Object> handleUnauthorizedException(UnAuthorizedException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(e.getErrorCode());
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<Object> handleForbiddenException(ForbiddenException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(e.getErrorCode());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Object> handleNotFoundException(NotFoundException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(e.getErrorCode());
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse<Object> handleConflictException(ConflictException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(e.getErrorCode());
    }

    @ExceptionHandler(BadGatewayException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ApiResponse<Object> handleBadGatewayException(BadGatewayException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(BAD_GATEWAY_EXCEPTION);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Object> handleInternalServerException(IllegalArgumentException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(INTERNAL_SERVER_EXCEPTION);
    }

}
