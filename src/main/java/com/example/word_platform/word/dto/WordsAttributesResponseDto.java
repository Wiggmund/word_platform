package com.example.word_platform.word.dto;

public record WordsAttributesResponseDto (
        Long attributeId,
        String name,
        String type,
        String value
) {}
