package com.potato.controller;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    public static final ApiResponse<String> OK = new ApiResponse<>("", "", "OK");

    private final String code;
    private final String message;
    private final T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("", "", data);
    }

    public static <T> ApiResponse<T> error(String code, String message) {
        return new ApiResponse<>(code, message, null);
    }

}
