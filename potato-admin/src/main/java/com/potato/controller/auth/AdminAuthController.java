package com.potato.controller.auth;

import com.potato.controller.ApiResponse;
import com.potato.service.AdminAuthService;
import com.potato.service.dto.request.GoogleAuthRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    @GetMapping("/admin/v1/auth/google")
    public ApiResponse<String> handleAdminGoogleAuthentication(@Valid GoogleAuthRequest request) {
        return ApiResponse.success(adminAuthService.handleGoogleAuthentication(request));
    }

}
