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
        return new ApiResponse<>(VALIDATION_EXCEPTION.getCode(), VALIDATION_EXCEPTION.getMessage(), e.getData());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String field = e.getBindingResult().getFieldError() == null ? "" : e.getBindingResult().getFieldError().getField();
        return new ApiResponse<>(VALIDATION_EXCEPTION.getCode(), String.format("(%s) %s", field, e.getBindingResult().getFieldError().getDefaultMessage()), null);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Object> handleBindException(BindException e) {
        log.error(e.getMessage(), e);
        String field = e.getBindingResult().getFieldError() == null ? "" : e.getBindingResult().getFieldError().getField();
        return new ApiResponse<>(VALIDATION_EXCEPTION.getCode(), String.format("(%s) %s", field, e.getBindingResult().getFieldError().getDefaultMessage()), null);
    }

    @ExceptionHandler(UnAuthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<Object> handleUnauthorizedException(UnAuthorizedException e) {
        log.error(e.getMessage(), e);
        return new ApiResponse<>(UNAUTHORIZED_EXCEPTION.getCode(), UNAUTHORIZED_EXCEPTION.getMessage(), e.getData());
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<Object> handleForbiddenException(ForbiddenException e) {
        log.error(e.getMessage(), e);
        return new ApiResponse<>(FORBIDDEN_EXCEPTION.getCode(), FORBIDDEN_EXCEPTION.getMessage(), e.getData());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Object> handleNotFoundException(NotFoundException e) {
        log.error(e.getMessage(), e);
        return new ApiResponse<>(NOT_FOUND_EXCEPTION.getCode(), NOT_FOUND_EXCEPTION.getMessage(), e.getData());
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse<Object> handleConflictException(ConflictException e) {
        log.error(e.getMessage(), e);
        return new ApiResponse<>(CONFLICT_EXCEPTION.getCode(), CONFLICT_EXCEPTION.getMessage(), e.getData());
    }

    @ExceptionHandler(BadGatewayException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ApiResponse<Object> handleBadGatewayException(BadGatewayException e) {
        log.error(e.getMessage(), e);
        return new ApiResponse<>(BAD_GATEWAY_EXCEPTION.getCode(), BAD_GATEWAY_EXCEPTION.getMessage(), e.getData());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Object> handleInternalServerException(IllegalArgumentException e) {
        log.error(e.getMessage(), e);
        return new ApiResponse<>(INTERNAL_SERVER_EXCEPTION.getCode(), INTERNAL_SERVER_EXCEPTION.getMessage(), null);
    }

}
