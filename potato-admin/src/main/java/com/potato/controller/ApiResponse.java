package com.potato.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor
public class ApiResponse<T> {

    public static final ApiResponse<String> OK = new ApiResponse<>("", "", "OK");

    private final String code;
    private final String message;
    private final T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("", "", data);
    }

}
