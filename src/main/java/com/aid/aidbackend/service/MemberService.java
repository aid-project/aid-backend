package com.aid.aidbackend.service;

import com.aid.aidbackend.dto.MemberDto;
import com.aid.aidbackend.entity.Member;
import com.aid.aidbackend.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member join(MemberDto memberDto) {
        Member member = Member.builder()
                .email(memberDto.email())
                .password(memberDto.password())
                .nickname(memberDto.nickname())
                .build();

        return memberRepository.save(member);
    }
}
