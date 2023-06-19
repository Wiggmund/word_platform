package com.example.word_platform.dto.word;

import com.example.word_platform.enums.AttributeType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record WordsAttributesCreateDto(
    @NotEmpty
    String name,
    @NotNull
    AttributeType type,
    @NotEmpty
    String value
) {
}
