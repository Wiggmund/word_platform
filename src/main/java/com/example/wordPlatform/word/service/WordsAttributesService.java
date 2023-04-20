package com.example.wordPlatform.word.service;

import com.example.wordPlatform.attribute.AttributeEntity;
import com.example.wordPlatform.attribute.AttributeService;
import com.example.wordPlatform.word.WordEntity;
import com.example.wordPlatform.word.WordsAttributesEntity;
import com.example.wordPlatform.word.WordsAttributesRepo;
import com.example.wordPlatform.word.dto.WordsAttributesCreateDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WordsAttributesService {
  private final WordsAttributesRepo wordsAttributesRepo;
  private final WordService wordService;
  private final AttributeService attributeService;

  public WordEntity updateAttributes(WordEntity word, List<WordsAttributesCreateDto> attributeDtos) {
    Map<String, String> newAttributeValues = attributeDtos.stream().collect(Collectors.toMap(
            WordsAttributesCreateDto::name,
            WordsAttributesCreateDto::value
    ));
    attributeService.getAttributesOrThrow(attributeDtos);

    List<WordsAttributesEntity> oldAttributes = wordsAttributesRepo.findAllByWord(word);
    oldAttributes.forEach(entry -> entry.setValue(newAttributeValues.get(entry.getAttribute().getName())));
    wordsAttributesRepo.saveAll(oldAttributes);
    return word;
  }
}
