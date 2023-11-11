package com.aid.aidbackend.controller;

import com.aid.aidbackend.auth.CurrentMember;
import com.aid.aidbackend.controller.dto.MemberDto;
import com.aid.aidbackend.service.MemberService;
import com.aid.aidbackend.utils.ApiResult;
import com.aid.aidbackend.utils.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import static com.aid.aidbackend.utils.ApiUtils.succeed;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;
    private final String successMessage = "success";

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
            successMessage
        );
    }

    @PatchMapping("/nickname")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ApiResult<String> changeNickname(
        HttpServletRequest httpServletRequest,
        @Valid @RequestBody ChangeNicknameRequest changeNicknameRequest
    ) {
        CurrentMember currentMember = (CurrentMember) httpServletRequest.getAttribute(JwtProvider.CURRENT_MEMBER);
        memberService.modifyNickname(currentMember.memberId(), changeNicknameRequest.nickname);

        return succeed(successMessage);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public ApiResult<String> removeMember(
            HttpServletRequest httpServletRequest
    ) {
        CurrentMember currentMember = (CurrentMember) httpServletRequest.getAttribute(JwtProvider.CURRENT_MEMBER);
        memberService.removeMember(currentMember.memberId());
        return succeed(successMessage);
    }

    record ChangePasswordRequest(
            @NotBlank String password,
            @NotBlank String new_password
    ) {
    }

    record ChangeNicknameRequest(
            @NotBlank String nickname
    ){
    }

}
