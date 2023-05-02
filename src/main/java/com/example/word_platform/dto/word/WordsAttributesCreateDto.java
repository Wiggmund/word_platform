package com.example.word_platform.dto.word;

import lombok.Builder;

@Builder
public record WordsAttributesCreateDto(
    String name,
    String type,
    String value
) {
}
