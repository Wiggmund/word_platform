package com.example.word_platform.attribute.dto;

import com.example.word_platform.attribute.AttributeEntity;
import com.example.word_platform.word.dto.WordsAttributesCreateDto;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AttributeWithValuesDto {
  private final Map<AttributeEntity, String> attributes;
  public AttributeWithValuesDto(
          List<AttributeEntity> attributes,
          List<WordsAttributesCreateDto> attributeDtos
  ) {
    Map<String, String> dtoAttributeValues = attributeDtos.stream().collect(Collectors.toMap(
            WordsAttributesCreateDto::name,
            WordsAttributesCreateDto::value
    ));

    this.attributes = attributes.stream().collect(Collectors.toMap(
            Function.identity(),
            entity -> dtoAttributeValues.get(entity.getName())
    ));
  }

  public Map<AttributeEntity, String> getAttributes() {
    return attributes;
  }
}
