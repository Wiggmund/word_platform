package com.example.word_platform.dto.question;

public record QuestionResponseDto (
        Long id,
        String text,
        String type,
        Long attributeId,
        Long wordlistId,
        Long userId
) {}
