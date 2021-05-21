package com.potato.external.external.google.dto.component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ToString
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "google.auth")
public class GoogleAccessTokenComponent {

    private String clientId;

    private String clientSecret;

    private String grantType;

    private String url;

}
