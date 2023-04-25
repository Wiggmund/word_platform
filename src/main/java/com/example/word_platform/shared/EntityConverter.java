package com.example.word_platform.shared;

import com.example.word_platform.dto.attribute.AttributeResponseDto;
import com.example.word_platform.dto.question.QuestionResponseDto;
import com.example.word_platform.dto.stats.StatsResponseDto;
import com.example.word_platform.dto.user.UserResponseDto;
import com.example.word_platform.dto.word.WordResponseDto;
import com.example.word_platform.dto.word.WordsAttributesResponseDto;
import com.example.word_platform.dto.wordlist.WordlistResponseDto;
import com.example.word_platform.model.Attribute;
import com.example.word_platform.model.Question;
import com.example.word_platform.model.Stats;
import com.example.word_platform.model.User;
import com.example.word_platform.model.Wordlist;
import com.example.word_platform.model.word.Word;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EntityConverter {
  public UserResponseDto entityToDto(User user) {
    return new UserResponseDto(
        user.getId(),
        user.getUsername(),
        user.getEmail()
    );
  }

  public WordlistResponseDto entityToDto(Wordlist wordlist) {
    return new WordlistResponseDto(
        wordlist.getId(),
        wordlist.getTitle(),
        wordlist.getDescription(),
        wordlist.getUser().getId()
    );
  }

  public QuestionResponseDto entityToDto(Question question) {
    return new QuestionResponseDto(
        question.getId(),
        question.getText(),
        question.getType(),
        question.getAnswer().getId(),
        question.getWordlist().getId(),
        question.getUser().getId()
    );
  }

  public AttributeResponseDto entityToDto(Attribute attribute) {
    return new AttributeResponseDto(
        attribute.getId(),
        attribute.getName(),
        attribute.getType()
    );
  }

  public WordResponseDto entityToDto(Word word) {
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

  public StatsResponseDto entityToDto(Stats entry) {
    Wordlist wordlist = entry.getWordlist();
    Word word = entry.getWord();
    Question question = entry.getQuestion();
    return new StatsResponseDto(
        entry.getId(),
        entry.getDate(),
        entry.getCorrect(),
        entry.getUser().getId(),
        wordlist == null ? null : wordlist.getId(),
        word == null ? null : word.getId(),
        question == null ? null : question.getId()
    );
  }
}
