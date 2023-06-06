package com.aid.aidbackend.controller;

import com.aid.aidbackend.dto.MemberDto;
import com.aid.aidbackend.entity.Member;
import com.aid.aidbackend.service.MemberService;
import com.aid.aidbackend.utils.ApiResult;
import com.aid.aidbackend.utils.ApiUtils;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/signup")
    public ApiResult<Member> createMember(@Valid @RequestBody MemberDto memberDto) {
        return ApiUtils.succeed(memberService.join(memberDto));
    }
}
