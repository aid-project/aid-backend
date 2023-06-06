package com.aid.aidbackend.service;

import com.aid.aidbackend.controller.dto.MemberDto;
import com.aid.aidbackend.entity.Member;
import com.aid.aidbackend.exception.DuplicateMemberException;
import com.aid.aidbackend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member join(MemberDto memberDto) {
        validateDuplicateEmail(memberDto.email());
        validateDuplicateNickname(memberDto.nickname());

        Member member = Member.builder()
                .email(memberDto.email())
                .password(passwordEncoder.encode(memberDto.password()))
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
