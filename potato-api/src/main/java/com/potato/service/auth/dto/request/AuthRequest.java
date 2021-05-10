package com.potato.service.auth.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthRequest {

    @NotBlank
    private String code;

    @NotBlank
    private String redirectUri;

    public static AuthRequest testInstance(String code, String redirectUri) {
        return new AuthRequest(code, redirectUri);
    }

}
