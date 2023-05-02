package com.example.word_platform.dto.word;

import com.example.word_platform.enums.AttributeType;

public record WordsAttributesResponseDto(
    Long attributeId,
    String name,
    AttributeType type,
    String value
) {
}
