package com.example.word_platform.dto.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record UserLoginDto(
    @NotEmpty
    String username,
    @NotEmpty
    String password
) {}
