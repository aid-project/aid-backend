package com.aid.aidbackend.service;

import com.aid.aidbackend.controller.dto.TokenDto;
import com.aid.aidbackend.entity.Member;
import com.aid.aidbackend.exception.WrongAuthDataException;
import com.aid.aidbackend.repository.MemberRepository;
import com.aid.aidbackend.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public TokenDto authenticate(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new WrongAuthDataException("이메일이 존재하지 않습니다."));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new WrongAuthDataException("비밀번호가 틀렸습니다.");
        }

        String memberId = String.valueOf(member.getId());

        return JwtUtils.generateToken(memberId);
    }

}
