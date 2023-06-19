package com.example.word_platform.dto.attribute;

import com.example.word_platform.enums.AttributeType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AttributeCreateDto(
    @NotEmpty
    String name,
    @NotNull
    AttributeType type
) {
}