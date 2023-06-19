package com.example.word_platform.dto.stats;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record StatsCreateDto(
    @NotNull
    @PastOrPresent
    LocalDate date,
    @NotNull
    Boolean correct,
    @NotNull
    @Positive
    Long wordId,
    @NotNull
    @Positive
    Long questionId
) {
}
