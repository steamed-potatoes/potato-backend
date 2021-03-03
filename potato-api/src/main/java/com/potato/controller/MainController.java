package com.potato.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @Operation(summary = "Health Check")
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

}
