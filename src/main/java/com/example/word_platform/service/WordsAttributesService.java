package com.example.word_platform.service;

import com.example.word_platform.dto.attribute.AttributeWithValuesDto;
import com.example.word_platform.model.word.Word;
import com.example.word_platform.model.word.WordsAttributes;
import com.example.word_platform.repository.WordsAttributesRepo;
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

  public Word updateAttributes(Word word, AttributeWithValuesDto receivedAttributesWithValues) {
    Map<String, String> receivedAttributeNamesAndValues = receivedAttributesWithValues.getAttributes().entrySet().stream()
            .collect(Collectors.toMap(
                    entry -> entry.getKey().getName(),
                    Map.Entry::getValue
            ));
    Set<String> receivedAttributeNames = receivedAttributeNamesAndValues.keySet();

    List<WordsAttributes> fetchedAttributes = wordsAttributesRepo.findAllByWord(word);
    fetchedAttributes.stream()
            .filter(entry -> {
              String oldAttributeName = entry.getAttribute().getName();
              return receivedAttributeNames.contains(oldAttributeName);
            })
            .forEach(entry -> {
              String oldAttributeName = entry.getAttribute().getName();
              String newAttributeValue = receivedAttributeNamesAndValues.get(oldAttributeName);
              entry.setValue(newAttributeValue);
            });

    wordsAttributesRepo.saveAll(fetchedAttributes);

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
