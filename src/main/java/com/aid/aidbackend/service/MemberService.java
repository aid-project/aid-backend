package com.aid.aidbackend.service;

import com.aid.aidbackend.dto.MemberDto;
import com.aid.aidbackend.entity.Member;
import com.aid.aidbackend.exception.DuplicateMemberException;
import com.aid.aidbackend.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member join(MemberDto memberDto) {
        validateDuplicateEmail(memberDto.email());
        validateDuplicateNickname(memberDto.nickname());

        Member member = Member.builder()
                .email(memberDto.email())
                .password(memberDto.password())
                .nickname(memberDto.nickname())
                .build();

        return memberRepository.save(member);
    }

    private void validateDuplicateEmail(String email) {
        if (memberRepository.findByEmail(email).orElse(null) != null) {
            throw new DuplicateMemberException("이미 존재하는 이메일입니다.");
        }
    }

    private void validateDuplicateNickname(String nickname) {
        if (memberRepository.findByNickname(nickname).orElse(null) != null) {
            throw new DuplicateMemberException("이미 존재하는 닉네임입니다.");
        }
    }
}
