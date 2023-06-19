package com.example.word_platform.dto.wordlist;

import jakarta.validation.constraints.NotEmpty;

public record WordlistCreateDto(
    @NotEmpty
    String title,
    @NotEmpty
    String description
) {
}
