package com.example.word_platform.dto.user;

import lombok.Builder;

@Builder
public record UserCreateDto(
    String username,
    String email,
    String password
) {
}