package com.example.word_platform.user.controller;

import com.example.word_platform.user.service.UserWordlistService;
import com.example.word_platform.wordlist.WordlistEntity;
import com.example.word_platform.wordlist.dto.WordlistCreateDto;
import com.example.word_platform.wordlist.dto.WordlistResponseDto;
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
@RequestMapping("api/v1/users/{userId}/wordlists")
@AllArgsConstructor
public class UserWordlistController {
  private final UserWordlistService userWordlistService;

  @GetMapping
  public ResponseEntity<List<WordlistResponseDto>> getAllUserWordlists(
          @PathVariable Long userId
  ) {
    List<WordlistResponseDto> fetchedWordlists = userWordlistService.getAllUserWordlists(userId)
            .stream()
            .map(WordlistResponseDto::new)
            .toList();

    return ResponseEntity.ok(fetchedWordlists);
  }

  @PostMapping
  public ResponseEntity<WordlistResponseDto> createUserWorldlist(
          @PathVariable Long userId,
          @RequestBody WordlistCreateDto dto
          ) {
    WordlistEntity createdWordlist = userWordlistService.createUserWorldlist(userId, dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(new WordlistResponseDto(createdWordlist));
  }
}
