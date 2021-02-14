package com.potato.external.google;

import com.potato.external.google.dto.component.GoogleAccessTokenComponent;
import com.potato.external.google.dto.component.GoogleUserInfoComponent;
import com.potato.external.google.dto.request.GoogleAccessTokenRequest;
import com.potato.external.google.dto.response.GoogleAccessTokenResponse;
import com.potato.external.google.dto.response.GoogleUserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class WebClientGoogleApiCallerImpl implements GoogleApiCaller {

    private final WebClient webClient;

    private final GoogleAccessTokenComponent googleAccessTokenComponent;
    private final GoogleUserInfoComponent googleUserInfoComponent;

    @Override
    public GoogleAccessTokenResponse getGoogleAccessToken(String code, String redirectUri) {
        return webClient.post()
            .uri(googleAccessTokenComponent.getUrl())
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .body(Mono.just(createGoogleAccessTokenRequest(code, redirectUri)), GoogleAccessTokenRequest.class)
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(IllegalArgumentException::new))
            .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(IllegalStateException::new))
            .bodyToMono(GoogleAccessTokenResponse.class)
            .block();
    }

    private GoogleAccessTokenRequest createGoogleAccessTokenRequest(String code, String redirectUri) {
        return GoogleAccessTokenRequest.builder()
            .clientId(googleAccessTokenComponent.getClientId())
            .clientSecret(googleAccessTokenComponent.getClientSecret())
            .grantType(googleAccessTokenComponent.getGrantType())
            .code(code)
            .redirectUri(redirectUri)
            .build();
    }

    @Override
    public GoogleUserInfoResponse getGoogleUserProfileInfo(String accessToken) {
        return webClient.get()
            .uri(googleUserInfoComponent.getUrl())
            .headers(headers -> headers.setBearerAuth(accessToken))
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(IllegalArgumentException::new))
            .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(IllegalStateException::new))
            .bodyToMono(GoogleUserInfoResponse.class)
            .block();
    }

}
