package com.example.wordPlatform.word.dto;

import java.util.List;

public record WordResponseDto(
        Long id,
        String value,
        Long userId,
        Long wordlistId,
        List<WordsAttributesResponseDto> attributes
) {}