package com.aid.aidbackend.service;

import com.aid.aidbackend.controller.dto.MemberRequest;
import com.aid.aidbackend.controller.dto.MemberResponse;
import com.aid.aidbackend.entity.Member;
import com.aid.aidbackend.exception.DuplicateMemberException;
import com.aid.aidbackend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member join(MemberRequest memberRequest) {
        validateDuplicateEmail(memberRequest.email());
        validateDuplicateNickname(memberRequest.nickname());

        return memberRepository.save(memberRequest.toMember(passwordEncoder));
    }

    public MemberResponse findOne(Long memberId) {
        return memberRepository.findById(memberId)
                .map(MemberResponse::of)
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));
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
