package com.example.word_platform.dto.question;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record QuestionUpdateDto(
    @NotEmpty
    String text,
    @NotEmpty
    String type,
    @NotNull
    @Positive
    Long attributeId
) {
}
