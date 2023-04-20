package com.example.wordPlatform.word.dto;

public record WordsAttributesResponseDto (
        Long attributeId,
        String name,
        String type,
        String value
) {}
