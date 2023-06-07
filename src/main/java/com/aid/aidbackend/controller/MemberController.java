package com.aid.aidbackend.controller;

import com.aid.aidbackend.controller.dto.MemberRequest;
import com.aid.aidbackend.controller.dto.MemberResponse;
import com.aid.aidbackend.entity.Member;
import com.aid.aidbackend.service.MemberService;
import com.aid.aidbackend.utils.ApiResult;
import com.aid.aidbackend.utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import static com.aid.aidbackend.utils.ApiUtils.succeed;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/signup")
    public ApiResult<Member> createMember(@Valid @RequestBody MemberRequest memberRequest) {
        return succeed(memberService.join(memberRequest));
    }

    @GetMapping()
    public ApiResult<MemberResponse> readMemberInfo() {
        return succeed(
                memberService.findOne(SecurityUtils.getCurrentMemberId())
        );
    }

}
