package com.example.word_platform.controller.user;

import com.example.word_platform.dto.wordlist.WordlistCreateDto;
import com.example.word_platform.dto.wordlist.WordlistResponseDto;
import com.example.word_platform.dto.wordlist.WordlistUpdateDto;
import com.example.word_platform.model.Wordlist;
import com.example.word_platform.service.user.UserWordlistService;
import com.example.word_platform.shared.EntityConverter;
import java.util.List;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users/{userId}/wordlists")
@AllArgsConstructor
public class UserWordlistController {
  private final UserWordlistService userWordlistService;
  private final EntityConverter entityConverter;

  @GetMapping
  public ResponseEntity<List<WordlistResponseDto>> getAllUserWordlists(
      @PathVariable Long userId
  ) {
    List<WordlistResponseDto> fetchedWordlists = userWordlistService.getAllUserWordlists(userId)
        .stream()
        .map(entityConverter::entityToDto)
        .toList();

    return ResponseEntity.ok(fetchedWordlists);
  }

  @GetMapping("{wordlistId}")
  public ResponseEntity<WordlistResponseDto> getUserWordlistById(
      @PathVariable Long userId,
      @PathVariable Long wordlistId
  ) {
    Wordlist wordlist = userWordlistService.getUserWordlistById(userId, wordlistId);
    return ResponseEntity.ok(entityConverter.entityToDto(wordlist));
  }

  @PostMapping
  public ResponseEntity<WordlistResponseDto> createUserWorldlist(
      @PathVariable Long userId,
      @Valid @RequestBody WordlistCreateDto dto
  ) {
    Wordlist createdWordlist = userWordlistService.createUserWorldlist(userId, dto);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(entityConverter.entityToDto(createdWordlist));
  }

  @PutMapping("{wordlistId}")
  public ResponseEntity<WordlistResponseDto> updateUserWordlist(
      @PathVariable Long userId,
      @PathVariable Long wordlistId,
      @Valid @RequestBody WordlistUpdateDto dto
  ) {
    Wordlist updatedWordlist = userWordlistService.updateUserWordlist(userId, wordlistId, dto);
    return ResponseEntity.ok(entityConverter.entityToDto(updatedWordlist));
  }

  @DeleteMapping("{wordlistId}")
  public ResponseEntity<WordlistResponseDto> removeUserWordlist(
      @PathVariable Long userId,
      @PathVariable Long wordlistId
  ) {
    Wordlist removedWordlist = userWordlistService.removeUserWordlist(userId, wordlistId);
    return ResponseEntity.ok(entityConverter.entityToDto(removedWordlist));
  }
}
