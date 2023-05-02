package com.example.word_platform.dto.attribute;

import com.example.word_platform.enums.AttributeType;

public record AttributeResponseDto(
    Long id,
    String name,
    AttributeType type
) {
}
