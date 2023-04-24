package com.example.word_platform.dto.wordlist;

public record WordlistResponseDto(
        Long id,
        String title,
        String description,
        Long userId
) {}
