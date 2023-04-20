package com.example.wordPlatform.word.dto;

import java.util.List;

public record WordAttributesUpdateDto(
        List<WordsAttributesCreateDto> attributes
) {}
