package com.potato.service.auth.dto.request;

import lombok.Builder;
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

    @Builder(builderMethodName = "testBuilder")
    public AuthRequest(@NotBlank String code, @NotBlank String redirectUri) {
        this.code = code;
        this.redirectUri = redirectUri;
    }

}
