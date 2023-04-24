package com.example.word_platform.dto.stats;

import java.time.LocalDate;

public record StatsCreateDto(
   LocalDate date,
   Boolean correct,
   Long wordId,
   Long questionId
) {}
