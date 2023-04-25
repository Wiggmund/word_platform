package com.example.word_platform.dto.attribute;

import com.example.word_platform.dto.word.WordsAttributesCreateDto;
import com.example.word_platform.model.Attribute;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AttributeWithValuesDto {
  private final Map<Attribute, String> attributes;

  public AttributeWithValuesDto(
      List<Attribute> attributes,
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

  public AttributeWithValuesDto(Map<Attribute, String> attributes) {
    this.attributes = attributes;
  }

  public Map<Attribute, String> getAttributes() {
    return attributes;
  }
}
