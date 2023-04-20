package com.example.wordPlatform.word.service;

import com.example.wordPlatform.word.WordEntity;
import com.example.wordPlatform.word.dto.WordResponseDto;
import com.example.wordPlatform.word.dto.WordsAttributesResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
public class WordResponseDtoMapper implements Function<WordEntity, WordResponseDto> {
  @Override
  public WordResponseDto apply(WordEntity word) {
    List<WordsAttributesResponseDto> wordsAttributesResponseDtos = word.getAttributes().stream()
            .map(item -> new WordsAttributesResponseDto(
                    item.getAttribute().getId(),
                    item.getAttribute().getName(),
                    item.getAttribute().getType(),
                    item.getValue()
            ))
            .toList();

    return new WordResponseDto(
            word.getId(),
            word.getValue(),
            word.getUser().getId(),
            word.getWordlist().getId(),
            wordsAttributesResponseDtos
    );
  }
}
