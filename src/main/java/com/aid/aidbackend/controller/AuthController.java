package com.aid.aidbackend.controller;

import com.aid.aidbackend.controller.dto.TokenDto;
import com.aid.aidbackend.service.AuthService;
import com.aid.aidbackend.service.MemberService;
import com.aid.aidbackend.utils.ApiResult;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.aid.aidbackend.utils.ApiUtils.succeed;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final MemberService memberService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public ApiResult<String> createMember(@Valid @RequestBody SignupRequest signupRequest) {
        memberService.join(
                signupRequest.email(),
                signupRequest.nickname(),
                signupRequest.password()
        );

        return succeed("success");
    }

    @PostMapping("/login")
    public ApiResult<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        TokenDto token = authService.authenticate(
                loginRequest.email(),
                loginRequest.password()
        );

        return succeed(token);
    }

    record SignupRequest(
            @Email @NotNull String email,
            @NotNull String nickname,
            @NotNull String password
    ) {
    }

    record LoginRequest(
            @NotNull
            String email,

            @NotNull
            String password
    ) {
    }
}
