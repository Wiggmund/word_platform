package com.example.word_platform.dto.attribute;

import com.example.word_platform.model.Attribute;

public record AttributeResponseDto(
        String name,
        String type
) {
  public AttributeResponseDto(Attribute attr) {
    this(
            attr.getName(),
            attr.getType()
    );
  }
}
