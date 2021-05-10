package com.potato.service.auth.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GoogleAuthRequest {

    @NotBlank
    private String code;

    @NotBlank
    private String redirectUri;

    public static GoogleAuthRequest testInstance(String code, String redirectUri) {
        return new GoogleAuthRequest(code, redirectUri);
    }

}
