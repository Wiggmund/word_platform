package com.example.wordPlatform.user.controller;

import com.example.wordPlatform.user.service.UserListWordService;
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
  private final UserListWordService userListWordService;
  private final WordResponseDtoMapper wordResponseDtoMapper;

  @GetMapping
  public ResponseEntity<List<WordResponseDto>> getAllWordlistWords(
          @PathVariable Long userId,
          @PathVariable Long wordlistId
  ) {
    List<WordResponseDto> response = userListWordService.getAllWordlistWords(userId, wordlistId).stream()
            .map(wordResponseDtoMapper)
            .toList();
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<Long> createAndAddWord(
          @PathVariable Long userId,
          @PathVariable Long wordlistId,
          @RequestBody WordCreateDto dto
  ) {
    Long createdWordId = userListWordService.createAndAddWord(userId, wordlistId, dto).getId();
    return ResponseEntity.status(HttpStatus.CREATED).body(createdWordId);
  }
}
