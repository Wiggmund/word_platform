package com.example.word_platform.controller.user;

import com.example.word_platform.service.user.UserWordService;
import com.example.word_platform.dto.word.WordResponseDto;
import com.example.word_platform.dto.dto_mappers.WordResponseDtoMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/users/{userId}/words")
@AllArgsConstructor
public class UserWordController {
  private final UserWordService userWordService;
  private final WordResponseDtoMapper wordResponseDtoMapper;

  @GetMapping
  public ResponseEntity<List<WordResponseDto>> getAllUserWords(@PathVariable Long userId) {
    List<WordResponseDto> content = userWordService.getAllUserWords(userId).stream()
            .map(wordResponseDtoMapper)
            .toList();

    return ResponseEntity.ok(content);
  }
}
