package com.potato.controller.auth;

import com.potato.controller.ApiResponse;
import com.potato.service.auth.AdminAuthService;
import com.potato.service.auth.dto.request.GoogleAuthRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    @PostMapping("/admin/v1/auth/google")
    public ApiResponse<String> handleAdminGoogleAuthentication(@Valid @RequestBody GoogleAuthRequest request) {
        return ApiResponse.success(adminAuthService.handleGoogleAuthentication(request));
    }

}
