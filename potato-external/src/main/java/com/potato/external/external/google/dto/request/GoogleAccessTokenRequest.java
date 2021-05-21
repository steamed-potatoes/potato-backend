package com.potato.external.external.google.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GoogleAccessTokenRequest {

    private final String clientId;

    private final String clientSecret;

    private final String grantType;

    private final String code;

    private final String redirectUri;

    @Builder
    public GoogleAccessTokenRequest(String clientId, String clientSecret, String grantType, String code, String redirectUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.grantType = grantType;
        this.code = code;
        this.redirectUri = redirectUri;
    }

}
