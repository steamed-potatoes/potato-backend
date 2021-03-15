package com.potato.service.auth.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@ToString
@Getter
@NoArgsConstructor
public class AuthRequest {

    @NotBlank
    private String code;

    @NotBlank
    private String redirectUri;

    private AuthRequest(@NotBlank String code, @NotBlank String redirectUri) {
        this.code = code;
        this.redirectUri = redirectUri;
    }

    public static AuthRequest testInstance(String code, String redirectUri) {
        return new AuthRequest(code, redirectUri);
    }

}
