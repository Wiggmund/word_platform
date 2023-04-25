package com.example.word_platform.dto.word;

public record WordsAttributesResponseDto(
    Long attributeId,
    String name,
    String type,
    String value
) {
}
