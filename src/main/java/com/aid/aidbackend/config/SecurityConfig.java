package com.aid.aidbackend.config;

import com.aid.aidbackend.jwt.JwtAccessDeniedHandler;
import com.aid.aidbackend.jwt.JwtAuthenticationEntryPoint;
import com.aid.aidbackend.jwt.JwtFilter;
import com.aid.aidbackend.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.aid.aidbackend.entity.Authority.ROLE_USER;
import static jakarta.servlet.DispatcherType.ERROR;
import static org.springframework.http.HttpMethod.POST;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(handlingConfigurer -> handlingConfigurer
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                .authorizeHttpRequests(matcherRegistry -> matcherRegistry
                        .dispatcherTypeMatchers(ERROR).permitAll() // Error dispatch 인가 예외
                        .requestMatchers(
                                "/api/v1/hello",
                                "/api/v1/members/signup",
                                "/api/v1/auth/login"
                        ).permitAll()
                        .requestMatchers(
                                POST, "/api/v1/members"
                        ).hasAuthority(ROLE_USER.name())
                        .anyRequest().denyAll()
                )
                .addFilterBefore(new JwtFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
