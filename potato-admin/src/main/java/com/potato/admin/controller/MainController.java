package com.potato.admin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/ping")
    public ApiResponse<String> ping() {
        return ApiResponse.success("Potato Admin Server OK");
    }

}
