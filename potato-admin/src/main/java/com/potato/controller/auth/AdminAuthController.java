package com.potato.controller.auth;

import com.potato.controller.ApiResponse;
import com.potato.service.AdminAuthService;
import com.potato.service.dto.request.GoogleAuthRequest;
import com.potato.service.dto.response.AdminAuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    // 프론트에서 google oauth2 공식문서 step1. Set authrization parameters에
    // https://accounts.google.com/o/oauth2/v2/auth + 파라미터 날리고 로그인 + 동의
    // redirect로 code가 날라온다.(쿼리스트링으로 날라옴)

    @GetMapping("/api/v1/admin/auth/google")
    public ApiResponse<AdminAuthResponse> adminGoogle(GoogleAuthRequest request) {
        return ApiResponse.success(adminAuthService.handlerGoogleAuth(request));
    }

    // 원래는 http://redirectUri?code={code} 이런식으로 날라옴 -> 프론트에서 바꿔줌
//    @GetMapping("")


}
