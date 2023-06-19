package com.example.word_platform.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record UserCreateDto(
    @NotEmpty
    String username,
    @NotEmpty
    @Email
    String email,
    @NotEmpty
    String password
) {
}