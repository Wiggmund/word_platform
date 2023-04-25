package com.example.word_platform.dto.question;

public record QuestionUpdateDto(
    String text,
    String type,
    Long attributeId
) {
}
