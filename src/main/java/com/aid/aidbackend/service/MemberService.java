package com.aid.aidbackend.service;

import com.aid.aidbackend.controller.dto.MemberDto;
import com.aid.aidbackend.entity.Member;
import com.aid.aidbackend.exception.DuplicateMemberException;
import com.aid.aidbackend.exception.WrongMemberException;
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

    public void join(String email, String nickname, String password) {
        validateDuplicateEmail(email);
        validateDuplicateNickname(nickname);

        Member member = Member.builder()
                .email(email)
                .nickname(nickname)
                .password(passwordEncoder.encode(password))
                .build();

        memberRepository.save(member);
    }

    public MemberDto findOne(Long memberId) {
        return memberRepository.findById(memberId)
                .map(MemberDto::of)
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));
    }

    public void modifyPassword(Long memberId, String password, String newPassword) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new WrongMemberException("비밀번호를 잘못 입력하셨습니다.");
        }
        member.updatePassword(passwordEncoder.encode(newPassword));
    }

    public void modifyNickname(Long memberId, String nickname) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));
        validateDuplicateNickname(nickname);
        member.updateNickname(nickname);
    }

    public void removeMember(Long memberId) {
        memberRepository.deleteById(memberId);
    }

    public void modifyProfile(Long memberId, String profileUrl) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));
        member.updateProfileUrl(profileUrl);
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
