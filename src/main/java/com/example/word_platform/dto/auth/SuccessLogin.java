package com.example.word_platform.dto.auth;

import com.example.word_platform.dto.role.RoleResponseDto;
import java.util.List;
import lombok.Builder;

@Builder
public record SuccessLogin(
    Long id,
    String username,
    String email,
    List<RoleResponseDto> roles,
    String accessToken,
    String refreshToken
) {}
