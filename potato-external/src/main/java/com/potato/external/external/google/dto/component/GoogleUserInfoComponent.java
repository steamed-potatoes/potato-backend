package com.potato.external.external.google.dto.component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ToString
@Getter
@Setter
@ConfigurationProperties(prefix = "google.profile")
@Component
public class GoogleUserInfoComponent {

    private String url;

}
