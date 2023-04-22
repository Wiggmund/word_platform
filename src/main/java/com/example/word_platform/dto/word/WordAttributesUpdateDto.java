package com.example.word_platform.dto.word;

import java.util.List;

public record WordAttributesUpdateDto(
        List<WordsAttributesCreateDto> attributes
) {}
