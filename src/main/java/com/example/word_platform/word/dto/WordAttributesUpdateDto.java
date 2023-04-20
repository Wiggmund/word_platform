package com.example.word_platform.word.dto;

import java.util.List;

public record WordAttributesUpdateDto(
        List<WordsAttributesCreateDto> attributes
) {}
