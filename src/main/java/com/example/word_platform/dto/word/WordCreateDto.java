package com.example.word_platform.dto.word;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record WordCreateDto(
    @NotEmpty
    String value,
    @NotNull
    List<WordsAttributesCreateDto> attributes
) {
}
