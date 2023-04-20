package com.example.word_platform.word.service;

import com.example.word_platform.word.WordEntity;
import com.example.word_platform.word.dto.WordResponseDto;
import com.example.word_platform.word.dto.WordsAttributesResponseDto;
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
