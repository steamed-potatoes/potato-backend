package com.potato.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

}
