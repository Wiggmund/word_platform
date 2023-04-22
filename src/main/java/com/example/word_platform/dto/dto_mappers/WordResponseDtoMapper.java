package com.example.word_platform.dto.dto_mappers;

import com.example.word_platform.model.Attribute;
import com.example.word_platform.model.User;
import com.example.word_platform.model.word.Word;
import com.example.word_platform.dto.word.WordResponseDto;
import com.example.word_platform.dto.word.WordsAttributesResponseDto;
import com.example.word_platform.model.Wordlist;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
public class WordResponseDtoMapper implements Function<Word, WordResponseDto> {
  @Override
  public WordResponseDto apply(Word word) {
    List<WordsAttributesResponseDto> wordsAttributesResponseDtos = word.getAttributes().stream()
            .map(item -> {
              Attribute attribute = item.getAttribute();
              return new WordsAttributesResponseDto(
                      attribute.getId(),
                      attribute.getName(),
                      attribute.getType(),
                      item.getValue()
              );
            })
            .toList();

    User user = word.getUser();
    Wordlist wordlist = word.getWordlist();
    return new WordResponseDto(
            word.getId(),
            word.getValue(),
            user == null ? null : user.getId(),
            wordlist == null ? null : wordlist.getId(),
            wordsAttributesResponseDtos
    );
  }
}
