package com.example.word_platform.dto.word;

import com.example.word_platform.enums.AttributeType;
import lombok.Builder;

@Builder
public record WordsAttributesCreateDto(
    String name,
    AttributeType type,
    String value
) {
}
