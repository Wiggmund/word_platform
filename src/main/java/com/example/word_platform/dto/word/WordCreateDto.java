package com.example.word_platform.dto.word;

import java.util.List;

public record WordCreateDto(
    String value,
    List<WordsAttributesCreateDto> attributes
) {
}
