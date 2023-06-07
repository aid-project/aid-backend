package com.aid.aidbackend.controller;

import com.aid.aidbackend.controller.dto.LoginRequestDto;
import com.aid.aidbackend.controller.dto.MemberRequest;
import com.aid.aidbackend.service.AuthService;
import com.aid.aidbackend.service.MemberService;
import com.aid.aidbackend.utils.ApiResult;
import jakarta.validation.Valid;
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
    public ApiResult<String> createMember(@Valid @RequestBody MemberRequest memberRequest) {
        memberService.join(memberRequest);
        return succeed("success");
    }

    @PostMapping("/login")
    public ApiResult<?> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return succeed(
                authService.authenticate(loginRequestDto.email(), loginRequestDto.password())
        );
    }
}
