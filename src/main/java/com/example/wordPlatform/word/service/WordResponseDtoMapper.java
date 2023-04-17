package com.example.wordPlatform.word.service;

import com.example.wordPlatform.word.WordEntity;
import com.example.wordPlatform.word.WordsAttributesEntity;
import com.example.wordPlatform.word.dto.WordResponseDto;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class WordResponseDtoMapper implements Function<WordEntity, WordResponseDto> {
  @Override
  public WordResponseDto apply(WordEntity word) {
    List<WordsAttributesEntity> attrs = word.getAttributes();
    Map<String, String> baseAttrs = attrs.stream()
            .filter(item -> item.getAttribute().getType().equalsIgnoreCase("base"))
            .collect(Collectors.toMap(
                    item -> item.getAttribute().getName(),
                    WordsAttributesEntity::getValue
            ));
    Map<String, String> customAttrs = attrs.stream()
            .filter(item -> item.getAttribute().getType().equalsIgnoreCase("custom"))
            .collect(Collectors.toMap(
                    item -> item.getAttribute().getName(),
                    WordsAttributesEntity::getValue
            ));

    Map<String, Map<String, String>> attrsByType = new HashMap<>();
    attrsByType.put("base", baseAttrs);
    attrsByType.put("custom", customAttrs);

    return new WordResponseDto(
            word.getId(),
            word.getValue(),
            word.getUser().getId(),
            word.getWordlist().getId(),
            attrsByType
    );
  }
}
