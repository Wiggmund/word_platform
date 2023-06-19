package com.example.word_platform.dto.word;

import jakarta.validation.constraints.NotEmpty;

public record WordUpdateDto(
    @NotEmpty
    String value
) {
}
