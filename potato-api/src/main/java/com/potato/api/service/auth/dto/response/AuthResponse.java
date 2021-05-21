package com.potato.api.service.auth.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthResponse {

    private AuthType type;

    private String email;

    private String name;

    private String profileUrl;

    private String token;

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
