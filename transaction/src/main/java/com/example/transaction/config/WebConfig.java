package com.example.transaction.config;

import com.example.transaction.security.JwtTokenAuthenticationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final JwtTokenAuthenticationInterceptor jwtTokenAuthenticationInterceptor;

    public WebConfig(JwtTokenAuthenticationInterceptor jwtTokenAuthenticationInterceptor) {
        this.jwtTokenAuthenticationInterceptor = jwtTokenAuthenticationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtTokenAuthenticationInterceptor);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*");
    }
}
