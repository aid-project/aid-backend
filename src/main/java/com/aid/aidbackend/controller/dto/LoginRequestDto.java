package com.aid.aidbackend.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record LoginRequestDto(
        @NotNull @Email String email,
        @NotNull String password
) {
}
