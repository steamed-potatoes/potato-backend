package com.potato.external.external.google;

import com.potato.external.external.google.dto.response.GoogleAccessTokenResponse;
import com.potato.external.external.google.dto.response.GoogleUserInfoResponse;

public interface GoogleApiCaller {

    GoogleAccessTokenResponse getGoogleAccessToken(String code, String redirectUri);

    GoogleUserInfoResponse getGoogleUserProfileInfo(String accessToken);

}
