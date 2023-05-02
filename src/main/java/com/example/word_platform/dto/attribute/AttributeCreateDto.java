package com.example.word_platform.dto.attribute;

import com.example.word_platform.enums.AttributeType;
import lombok.Builder;

@Builder
public record AttributeCreateDto(
    String name,
    AttributeType type
) {
}