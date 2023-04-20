package com.example.word_platform.attribute.dto;

import com.example.word_platform.attribute.AttributeEntity;

public record AttributeResponseDto(
        String name,
        String type
) {
  public AttributeResponseDto(AttributeEntity attr) {
    this(
            attr.getName(),
            attr.getType()
    );
  }
}
