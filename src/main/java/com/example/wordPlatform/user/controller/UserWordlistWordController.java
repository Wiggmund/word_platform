package com.example.wordPlatform.user.controller;

import com.example.wordPlatform.user.service.UserWordlistWordService;
import com.example.wordPlatform.word.WordEntity;
import com.example.wordPlatform.word.dto.WordCreateDto;
import com.example.wordPlatform.word.dto.WordResponseDto;
import com.example.wordPlatform.word.service.WordResponseDtoMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/users/{userId}/wordlists/{wordlistId}/words")
@AllArgsConstructor
public class UserWordlistWordController {
  private final UserWordlistWordService userWordlistWordService;
  private final WordResponseDtoMapper wordResponseDtoMapper;

  @GetMapping
  public ResponseEntity<List<WordResponseDto>> getAllWordlistWords(
          @PathVariable Long userId,
          @PathVariable Long wordlistId
  ) {
    List<WordResponseDto> response = userWordlistWordService.getAllWordlistWords(userId, wordlistId).stream()
            .map(wordResponseDtoMapper)
            .toList();
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<WordResponseDto> createAndAddWord(
          @PathVariable Long userId,
          @PathVariable Long wordlistId,
          @RequestBody WordCreateDto dto
  ) {
    WordEntity createdWord = userWordlistWordService.createAndAddWord(userId, wordlistId, dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(wordResponseDtoMapper.apply(createdWord));
  }
}
