package com.example.word_platform.dto.question;

public record QuestionCreateDto(
    String text,
    String type,
    Long attributeId
) {
}
