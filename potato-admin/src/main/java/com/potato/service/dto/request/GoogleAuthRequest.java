package com.potato.service.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class GoogleAuthRequest {

    @NotBlank
    private String code;

    @NotBlank
    private String redirectUri;

    private GoogleAuthRequest(@NotBlank String code, @NotBlank String redirectUri) {
        this.code = code;
        this.redirectUri = redirectUri;
    }

    public static GoogleAuthRequest testInstance(String code, String redirectUri) {
        return new GoogleAuthRequest(code, redirectUri);
    }

}
