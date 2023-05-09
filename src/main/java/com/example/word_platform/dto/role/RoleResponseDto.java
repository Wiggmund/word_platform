package com.example.word_platform.dto.role;

import lombok.Builder;

@Builder
public record RoleResponseDto(
    Long id,
    String name
) {}
