package com.example.word_platform.controller.user;

import com.example.word_platform.dto.word.WordCreateDto;
import com.example.word_platform.dto.word.WordResponseDto;
import com.example.word_platform.dto.word.WordUpdateDto;
import com.example.word_platform.dto.word.WordsAttributesCreateDto;
import com.example.word_platform.model.word.Word;
import com.example.word_platform.service.user.UserWordlistWordService;
import com.example.word_platform.shared.EntityConverter;
import java.util.List;
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
@RequestMapping("api/v1/users/{userId}/wordlists/{wordlistId}/words")
@AllArgsConstructor
public class UserWordlistWordController {
  private final UserWordlistWordService userWordlistWordService;
  private final EntityConverter entityConverter;

  @GetMapping
  public ResponseEntity<List<WordResponseDto>> getAllWordlistWords(
      @PathVariable Long userId,
      @PathVariable Long wordlistId
  ) {
    List<WordResponseDto> response =
        userWordlistWordService.getAllWordlistWords(userId, wordlistId).stream()
            .map(entityConverter::entityToDto)
            .toList();
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<WordResponseDto> createAndAddWord(
      @PathVariable Long userId,
      @PathVariable Long wordlistId,
      @RequestBody WordCreateDto dto
  ) {
    Word createdWord = userWordlistWordService.createAndAddWord(userId, wordlistId, dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(entityConverter.entityToDto(createdWord));
  }

  @PutMapping("{wordId}")
  public ResponseEntity<WordResponseDto> updateWord(
      @PathVariable Long userId,
      @PathVariable Long wordlistId,
      @PathVariable Long wordId,
      @RequestBody WordUpdateDto dto
  ) {
    Word updatedWord = userWordlistWordService.updateWord(userId, wordlistId, wordId, dto);
    return ResponseEntity.ok(entityConverter.entityToDto(updatedWord));
  }

  @PutMapping("{wordId}/attributes")
  public ResponseEntity<WordResponseDto> updateWordAttributes(
      @PathVariable Long userId,
      @PathVariable Long wordlistId,
      @PathVariable Long wordId,
      @RequestBody List<WordsAttributesCreateDto> dto
  ) {
    Word updatedWord =
        userWordlistWordService.updateWordAttributes(userId, wordlistId, wordId, dto);
    return ResponseEntity.ok(entityConverter.entityToDto(updatedWord));
  }

  @DeleteMapping("{wordId}")
  public ResponseEntity<WordResponseDto> deleteWord(
      @PathVariable Long userId,
      @PathVariable Long wordlistId,
      @PathVariable Long wordId
  ) {
    Word removedWord = userWordlistWordService.removeWord(userId, wordlistId, wordId);
    return ResponseEntity.ok(entityConverter.entityToDto(removedWord));
  }
}
