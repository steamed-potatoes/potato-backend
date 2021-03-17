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

}
