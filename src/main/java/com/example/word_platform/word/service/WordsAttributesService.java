package com.example.word_platform.word.service;

import com.example.word_platform.attribute.AttributeService;
import com.example.word_platform.attribute.dto.AttributeWithValuesDto;
import com.example.word_platform.word.WordEntity;
import com.example.word_platform.word.WordsAttributesEntity;
import com.example.word_platform.word.WordsAttributesRepo;
import com.example.word_platform.word.dto.WordsAttributesCreateDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WordsAttributesService {
  private final WordsAttributesRepo wordsAttributesRepo;
  private final AttributeService attributeService;

  public WordEntity updateAttributes(WordEntity word, List<WordsAttributesCreateDto> attributeDtos) {
    attributeService.getAttributesFromOrThrow(attributeDtos);

    Map<String, String> attributeDtoNamesAndValues = attributeDtos.stream().collect(Collectors.toMap(
            WordsAttributesCreateDto::name,
            WordsAttributesCreateDto::value
    ));
    Set<String> attributeDtoNames = attributeDtoNamesAndValues.keySet();

    List<WordsAttributesEntity> oldAttributes = wordsAttributesRepo.findAllByWord(word);
    oldAttributes.stream()
            .filter(entry -> attributeDtoNames.contains(entry.getAttribute().getName()))
            .forEach(entry -> entry.setValue(attributeDtoNamesAndValues.get(entry.getAttribute().getName())));

    wordsAttributesRepo.saveAll(oldAttributes);

    return word;
  }

  public WordEntity addAttributes(WordEntity word, AttributeWithValuesDto wordAttributes) {
    List<WordsAttributesEntity> attributes = wordAttributes.getAttributes().entrySet().stream()
            .map(item -> word.addAttribute(item.getKey(), item.getValue()))
            .toList();
    word.setAttributes(attributes);
    return word;
  }
}
