package com.example.word_platform.service;

import com.example.word_platform.dto.attribute.AttributeWithValuesDto;
import com.example.word_platform.model.word.Word;

public interface WordsAttributesService {
  Word updateAttributes(Word word, AttributeWithValuesDto receivedAttributesWithValues);

  Word addAttributes(Word word, AttributeWithValuesDto wordAttributes);
}
