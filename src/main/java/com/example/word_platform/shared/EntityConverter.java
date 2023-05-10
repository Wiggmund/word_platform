package com.example.word_platform.shared;

import com.example.word_platform.dto.attribute.AttributeResponseDto;
import com.example.word_platform.dto.question.QuestionResponseDto;
import com.example.word_platform.dto.role.RoleResponseDto;
import com.example.word_platform.dto.stats.StatsResponseDto;
import com.example.word_platform.dto.user.UserResponseDto;
import com.example.word_platform.dto.word.WordResponseDto;
import com.example.word_platform.dto.word.WordsAttributesResponseDto;
import com.example.word_platform.dto.wordlist.WordlistResponseDto;
import com.example.word_platform.model.Attribute;
import com.example.word_platform.model.Question;
import com.example.word_platform.model.Role;
import com.example.word_platform.model.Stats;
import com.example.word_platform.model.AppUser;
import com.example.word_platform.model.Wordlist;
import com.example.word_platform.model.word.Word;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EntityConverter {
  public UserResponseDto entityToDto(AppUser user) {
    log.debug("Transforming user {} to DTO", user);
    return new UserResponseDto(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        entityToDto(user.getRoles())
    );
  }

  public List<RoleResponseDto> entityToDto(List<Role> roles) {
    return roles.stream()
        .map(item -> RoleResponseDto.builder()
            .id(item.getId())
            .name(item.getName())
            .build())
        .toList();
  }

  public WordlistResponseDto entityToDto(Wordlist wordlist) {
    log.debug("Transforming wordlist {} to DTO", wordlist);
    return new WordlistResponseDto(
        wordlist.getId(),
        wordlist.getTitle(),
        wordlist.getDescription(),
        wordlist.getUser().getId()
    );
  }

  public QuestionResponseDto entityToDto(Question question) {
    log.debug("Transforming question {} to DTO", question);
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
    log.debug("Transforming attribute {} to DTO", attribute);
    return new AttributeResponseDto(
        attribute.getId(),
        attribute.getName(),
        attribute.getType()
    );
  }

  public WordResponseDto entityToDto(Word word) {
    log.debug("Transforming word {} to DTO", word);
    List<WordsAttributesResponseDto> wordsAttributesResponseDtos = word.getAttributes().stream()
        .map(item -> {
          Attribute attribute = item.getAttribute();
          return new WordsAttributesResponseDto(
              attribute.getId(),
              attribute.getName(),
              attribute.getType(),
              item.getText()
          );
        })
        .toList();

    AppUser user = word.getUser();
    Wordlist wordlist = word.getWordlist();
    return new WordResponseDto(
        word.getId(),
        word.getDefinition(),
        user == null ? null : user.getId(),
        wordlist == null ? null : wordlist.getId(),
        wordsAttributesResponseDtos
    );
  }

  public StatsResponseDto entityToDto(Stats entry) {
    log.debug("Transforming stats {} to DTO", entry);
    Wordlist wordlist = entry.getWordlist();
    Word word = entry.getWord();
    Question question = entry.getQuestion();
    return new StatsResponseDto(
        entry.getId(),
        entry.getTestingDate(),
        entry.getCorrect(),
        entry.getUser().getId(),
        wordlist == null ? null : wordlist.getId(),
        word == null ? null : word.getId(),
        question == null ? null : question.getId()
    );
  }
}
