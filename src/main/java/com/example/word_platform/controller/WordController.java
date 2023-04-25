package com.example.word_platform.controller;

import com.example.word_platform.dto.word.WordResponseDto;
import com.example.word_platform.service.WordService;
import com.example.word_platform.shared.EntityConverter;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/words")
@AllArgsConstructor
public class WordController {
  private final WordService wordService;
  private final EntityConverter entityConverter;

  @GetMapping
  public ResponseEntity<List<WordResponseDto>> getAllWords() {
    List<WordResponseDto> words = wordService.getAllWords().stream()
        .map(entityConverter::entityToDto)
        .toList();

    return ResponseEntity.ok(words);
  }
}
