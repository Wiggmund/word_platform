package com.example.word_platform.word.service;

import com.example.word_platform.attribute.AttributeEntity;
import com.example.word_platform.user.UserEntity;
import com.example.word_platform.word.WordEntity;
import com.example.word_platform.word.dto.WordResponseDto;
import com.example.word_platform.word.dto.WordsAttributesResponseDto;
import com.example.word_platform.wordlist.WordlistEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
public class WordResponseDtoMapper implements Function<WordEntity, WordResponseDto> {
  @Override
  public WordResponseDto apply(WordEntity word) {
    List<WordsAttributesResponseDto> wordsAttributesResponseDtos = word.getAttributes().stream()
            .map(item -> {
              AttributeEntity attribute = item.getAttribute();
              return new WordsAttributesResponseDto(
                      attribute.getId(),
                      attribute.getName(),
                      attribute.getType(),
                      item.getValue()
              );
            })
            .toList();

    UserEntity user = word.getUser();
    WordlistEntity wordlist = word.getWordlist();
    return new WordResponseDto(
            word.getId(),
            word.getValue(),
            user == null ? null : user.getId(),
            wordlist == null ? null : wordlist.getId(),
            wordsAttributesResponseDtos
    );
  }
}
