package com.example.wordPlatform.word.dto;

import java.util.List;

public record WordCreateDto(
        String value,
        List<WordsAttributesEntryDto> attributes
) {}
