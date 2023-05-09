package com.example.word_platform.dto.user;

import lombok.Builder;

@Builder
public record UserLoginDto(
    String username,
    String password
) {}
