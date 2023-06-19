package com.example.word_platform.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record UserUpdateDto(
    @NotEmpty
    String username,
    @NotEmpty
    @Email
    String email
) {
}
