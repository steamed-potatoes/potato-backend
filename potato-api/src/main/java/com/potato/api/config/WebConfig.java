package com.potato.api.config;

import com.potato.api.config.argumentResolver.MemberIdResolver;
import com.potato.api.config.interceptor.auth.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final MemberIdResolver memberIdResolver;
    private final AuthInterceptor authInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins(
                "https://ourpotato.netlify.app", "https://potato-front.netlify.app", "https://potato-admin.netlify.app",
                "http://localhost:3000", "http://localhost:6500"
            )
            .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS", "PATCH")
            .allowedHeaders("*")
            .allowCredentials(true);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberIdResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor);
    }

}
