package com.example.word_platform.dto.attribute;

import lombok.Builder;

@Builder
public record AttributeCreateDto(
    String name,
    String type
) {
}