package com.aid.aidbackend.controller;

import com.aid.aidbackend.auth.CurrentMember;
import com.aid.aidbackend.controller.dto.MemberDto;
import com.aid.aidbackend.service.FileService;
import com.aid.aidbackend.service.MemberService;
import com.aid.aidbackend.utils.ApiResult;
import com.aid.aidbackend.utils.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.aid.aidbackend.utils.ApiUtils.succeed;
import static com.aid.aidbackend.utils.JwtProvider.CURRENT_MEMBER;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final FileService fileService;
    private static final String SUCCESS_MESSAGE = "success";


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
                SUCCESS_MESSAGE
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

        return succeed(SUCCESS_MESSAGE);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public ApiResult<String> removeMember(
            HttpServletRequest httpServletRequest
    ) {
        CurrentMember currentMember = (CurrentMember) httpServletRequest.getAttribute(JwtProvider.CURRENT_MEMBER);
        memberService.removeMember(currentMember.memberId());
        return succeed(SUCCESS_MESSAGE);
    }

    @PostMapping("/profile")
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResult<String> changeProfile(
            HttpServletRequest httpServletRequest,
            @RequestParam(name = "profile_img") MultipartFile file
    ) {
        CurrentMember currentMember = (CurrentMember) httpServletRequest.getAttribute(CURRENT_MEMBER);

        String profileUrl = fileService.upload(file);
        memberService.modifyProfile(currentMember.memberId(), profileUrl);

        return succeed(SUCCESS_MESSAGE);
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
