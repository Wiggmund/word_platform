package com.example.word_platform.dto.auth;

import com.example.word_platform.dto.role.RoleResponseDto;
import java.util.List;
import lombok.Builder;

@Builder
public record SuccessRegistration(
    Long id,
    String username,
    String email,
    List<RoleResponseDto> roles
) {}
