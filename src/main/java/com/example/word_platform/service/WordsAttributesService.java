package com.example.word_platform.service;

import com.example.word_platform.dto.attribute.AttributeWithValuesDto;
import com.example.word_platform.model.word.Word;
import com.example.word_platform.model.word.WordsAttributes;
import com.example.word_platform.repository.WordsAttributesRepo;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class WordsAttributesService {
  private final WordsAttributesRepo wordsAttributesRepo;

  public Word updateAttributes(Word word, AttributeWithValuesDto receivedAttributesWithValues) {
    log.debug("Updating attributes for word {}", word);
    Map<String, String> receivedAttributeNamesAndValues =
        receivedAttributesWithValues.getAttributes().entrySet().stream()
            .collect(Collectors.toMap(
                entry -> entry.getKey().getName(),
                Map.Entry::getValue
            ));
    Set<String> receivedAttributeNames = receivedAttributeNamesAndValues.keySet();
    log.debug("Word {} attributes to update {}", word, receivedAttributeNames);

    List<WordsAttributes> fetchedAttributes = wordsAttributesRepo.findAllByWord(word);
    fetchedAttributes.stream()
        .filter(entry -> {
          String oldAttributeName = entry.getAttribute().getName();
          return receivedAttributeNames.contains(oldAttributeName);
        })
        .forEach(entry -> {
          String oldAttributeName = entry.getAttribute().getName();
          String newAttributeValue = receivedAttributeNamesAndValues.get(oldAttributeName);
          entry.setText(newAttributeValue);
        });

    wordsAttributesRepo.saveAll(fetchedAttributes);

    log.debug("Attributes for word {} were updated: {}", word, fetchedAttributes);
    return word;
  }

  public Word addAttributes(Word word, AttributeWithValuesDto wordAttributes) {
    log.debug("Adding attributes to word {}", word);

    List<WordsAttributes> attributes = wordAttributes.getAttributes().entrySet().stream()
        .map(item -> word.addAttribute(item.getKey(), item.getValue()))
        .toList();

    word.setAttributes(attributes);
    log.debug("{} attributes were added to word {}", attributes.size(), word);

    return word;
  }
}
