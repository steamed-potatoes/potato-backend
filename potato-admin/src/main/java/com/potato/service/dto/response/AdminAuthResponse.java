package com.potato.service.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminAuthResponse {

    private final String email;

    private final String name;

    private final String token;

    public static AdminAuthResponse login(String token) {
        return new AdminAuthResponse(null, null, token);
    }

}
