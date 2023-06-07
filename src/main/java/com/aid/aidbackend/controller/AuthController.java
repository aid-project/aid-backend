package com.aid.aidbackend.controller;

import com.aid.aidbackend.controller.dto.LoginRequestDto;
import com.aid.aidbackend.service.AuthService;
import com.aid.aidbackend.utils.ApiResult;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.aid.aidbackend.utils.ApiUtils.succeed;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResult<?> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return succeed(
                authService.authenticate(loginRequestDto.email(), loginRequestDto.password())
        );
    }
}
