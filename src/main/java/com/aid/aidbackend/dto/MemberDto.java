package com.aid.aidbackend.dto;

public record MemberDto(
        String email,
        String nickname,
        String password
) {

}