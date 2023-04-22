package com.example.word_platform.service;

import com.example.word_platform.dto.attribute.AttributeWithValuesDto;
import com.example.word_platform.model.word.Word;
import com.example.word_platform.model.word.WordsAttributes;
import com.example.word_platform.repository.WordsAttributesRepo;
import com.example.word_platform.dto.word.WordsAttributesCreateDto;
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

  public Word updateAttributes(Word word, List<WordsAttributesCreateDto> attributeDtos) {
    attributeService.getAttributesFromOrThrow(attributeDtos);

    Map<String, String> attributeDtoNamesAndValues = attributeDtos.stream().collect(Collectors.toMap(
            WordsAttributesCreateDto::name,
            WordsAttributesCreateDto::value
    ));
    Set<String> attributeDtoNames = attributeDtoNamesAndValues.keySet();

    List<WordsAttributes> oldAttributes = wordsAttributesRepo.findAllByWord(word);
    oldAttributes.stream()
            .filter(entry -> attributeDtoNames.contains(entry.getAttribute().getName()))
            .forEach(entry -> entry.setValue(attributeDtoNamesAndValues.get(entry.getAttribute().getName())));

    wordsAttributesRepo.saveAll(oldAttributes);

    return word;
  }

  public Word addAttributes(Word word, AttributeWithValuesDto wordAttributes) {
    List<WordsAttributes> attributes = wordAttributes.getAttributes().entrySet().stream()
            .map(item -> word.addAttribute(item.getKey(), item.getValue()))
            .toList();
    word.setAttributes(attributes);
    return word;
  }
}
