package com.aid.aidbackend.controller;

import com.aid.aidbackend.auth.CurrentMember;
import com.aid.aidbackend.controller.dto.MemberDto;
import com.aid.aidbackend.service.MemberService;
import com.aid.aidbackend.utils.ApiResult;
import com.aid.aidbackend.utils.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import static com.aid.aidbackend.utils.ApiUtils.succeed;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping()
    public ApiResult<MemberDto> readMemberInfo(HttpServletRequest httpServletRequest) {
        CurrentMember currentMember = (CurrentMember) httpServletRequest.getAttribute(JwtProvider.CURRENT_MEMBER);
        return succeed(
                memberService.findOne(currentMember.memberId())
        );
    }
    @PatchMapping("/password")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ApiResult<String> changePassword(
            HttpServletRequest httpServletRequest,
            @Valid @RequestBody ChangePasswordRequest changePasswordRequest
    ) {
        CurrentMember currentMember = (CurrentMember) httpServletRequest.getAttribute(JwtProvider.CURRENT_MEMBER);
        memberService.modifyPassword(
                currentMember.memberId(),
                changePasswordRequest.password,
                changePasswordRequest.new_password
        );

        return succeed(
            "success"
        );
    }
    record ChangePasswordRequest(
            @NotNull String password,
            @NotNull String new_password
    ) {
    }

}
