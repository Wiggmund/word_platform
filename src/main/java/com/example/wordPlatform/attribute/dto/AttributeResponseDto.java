package com.example.wordPlatform.attribute.dto;

import com.example.wordPlatform.attribute.AttributeEntity;

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
