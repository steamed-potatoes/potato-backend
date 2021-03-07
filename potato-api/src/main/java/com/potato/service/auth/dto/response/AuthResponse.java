package com.potato.service.auth.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthResponse {

    private final AuthType type;

    private final String email;

    private final String name;

    private final String profileUrl;

    private final String token;

    public static AuthResponse signUp(String email, String name, String profileUrl) {
        return new AuthResponse(AuthType.SIGN_UP, email, name, profileUrl, null);
    }

    public static AuthResponse login(String token) {
        return new AuthResponse(AuthType.LOGIN, null, null, null, token);
    }

    public enum AuthType {
        SIGN_UP,
        LOGIN
    }

}
