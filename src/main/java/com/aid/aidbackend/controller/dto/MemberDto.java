package com.aid.aidbackend.controller.dto;

import com.aid.aidbackend.entity.Member;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MemberDto(
        String email,
        String nickname,
        String profileUrl,
        LocalDateTime registeredAt
) {

    public static MemberDto of(Member member) {
        return new MemberDto(
                member.getEmail(),
                member.getNickname(),
                member.getProfileUrl(),
                member.getRegisteredAt()
        );
    }
}
