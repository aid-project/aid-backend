package com.aid.aidbackend.config;

import com.aid.aidbackend.auth.interceptor.JwtInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/v1/members/**")
                .addPathPatterns("/api/v1/drawings/**")
                .excludePathPatterns("/api/v1/hello")
                .excludePathPatterns("/api/v1/auth/**")
                .excludePathPatterns("/api/v1/drawings/list");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://projectaidraw.s3-website-us-east-1.amazonaws.com/");
    }
}
