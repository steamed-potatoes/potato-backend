package com.potato.controller;

import com.potato.config.interceptor.Auth;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/ping")
    public ApiResponse<String> ping() {
        return ApiResponse.OK;
    }

    @Auth
    @GetMapping("/auth")
    public ApiResponse<String> auth() {
        return ApiResponse.OK;
    }

}
