package com.example.word_platform.dto.auth;

import lombok.Builder;

@Builder
public record AuthenticationResponse(
    String accessToken
) {}
