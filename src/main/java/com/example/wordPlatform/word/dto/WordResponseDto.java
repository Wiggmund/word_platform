package com.example.wordPlatform.word.dto;

import java.util.Map;

public record WordResponseDto(
        Long id,
        String value,
        Long userId,
        Long wordlistId,
        Map<String, Map<String, String>> attributes
) {}