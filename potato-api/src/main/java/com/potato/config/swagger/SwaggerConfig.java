package com.potato.config.swagger;

import com.potato.config.argumentResolver.MemberId;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String securityKey = "BearerKey";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .components(new Components()
                .addSecuritySchemes(securityKey,
                    new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
            .info(new Info()
                .title("Potato API Server")
                .description("Potato API Documents"));
    }

    static {
        SpringDocUtils.getConfig().addAnnotationsToIgnore(MemberId.class);
    }

}
