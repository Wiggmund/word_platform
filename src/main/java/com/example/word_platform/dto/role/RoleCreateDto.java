package com.example.word_platform.dto.role;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record RoleCreateDto(
    @NotEmpty
    String name
) {}
