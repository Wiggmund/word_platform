package com.example.word_platform.dto.auth;

import lombok.Builder;

@Builder
public record RefreshTokenResponse(
    String accessToken,
    String refreshToken
) {}
