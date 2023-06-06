package com.aid.aidbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(matcherRegistry -> matcherRegistry
                        .requestMatchers(
                                "/api/v1/hello"
                        ).permitAll()
                        .anyRequest().authenticated()
                );

        return httpSecurity.build();
    }
}
