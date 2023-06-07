package com.aid.aidbackend.controller.dto;


import com.aid.aidbackend.entity.Authority;
import com.aid.aidbackend.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;

public record MemberRequest(
        @Email @NotNull String email,
        @NotNull String nickname,
        @NotNull String password
) {
    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .password(passwordEncoder.encode(password))
                .authority((Authority.ROLE_USER))
                .build();
    }
}