package com.example.word_platform.dto.wordlist;

import jakarta.validation.constraints.NotEmpty;

public record WordlistUpdateDto(
    @NotEmpty
    String title,
    @NotEmpty
    String description
) {
}
