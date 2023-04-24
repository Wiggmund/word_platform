package com.example.word_platform.dto.stats;

import java.time.LocalDate;
public record StatsResponseDto(
        Long id,
        LocalDate time,
        Boolean correct,
        Long userId,
        Long wordlistId,
        Long wordId,
        Long questionId
) {}
