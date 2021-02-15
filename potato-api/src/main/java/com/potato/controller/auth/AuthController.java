package com.potato.controller.auth;

import com.potato.controller.ApiResponse;
import com.potato.service.auth.GoogleAuthService;
import com.potato.service.auth.dto.request.AuthRequest;
import com.potato.service.auth.dto.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final GoogleAuthService googleAuthService;

    @PostMapping("/api/v1/auth/google")
    public ApiResponse<AuthResponse> handleGoogleAuthentication(@Valid @RequestBody AuthRequest request) {
        return ApiResponse.of(googleAuthService.handleGoogleAuthentication(request));
    }

}
