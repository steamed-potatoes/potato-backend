package com.potato.service.dto.request;

import lombok.Getter;

@Getter
public class GoogleAuthRequest {

    private String code;

    private String redirectUri;

}
