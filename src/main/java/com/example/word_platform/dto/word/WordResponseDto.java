package com.example.word_platform.dto.word;

import java.util.List;

public record WordResponseDto(
    Long id,
    String value,
    Long userId,
    Long wordlistId,
    List<WordsAttributesResponseDto> attributes
) {
}