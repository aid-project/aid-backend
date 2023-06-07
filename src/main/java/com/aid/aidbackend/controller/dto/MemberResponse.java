package com.aid.aidbackend.controller.dto;

import com.aid.aidbackend.entity.Member;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MemberResponse(
        String email,
        String nickname,
        String profileUrl,
        LocalDateTime registeredAt
) {

    public static MemberResponse of(Member member) {
        return new MemberResponse(
                member.getEmail(),
                member.getNickname(),
                member.getProfileUrl(),
                member.getRegisteredAt()
        );
    }
}
