package com.example.word_platform.dto.stats;

import java.util.List;

public record StatsPackagedCreateDto(
        List<StatsCreateDto> items
) {}
