package com.example.word_platform.controller;

import com.example.word_platform.dto.word.WordResponseDto;
import com.example.word_platform.dto.dto_mappers.WordResponseDtoMapper;
import com.example.word_platform.service.WordService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/words")
@AllArgsConstructor
public class WordController {
  private final WordService wordService;
  private final WordResponseDtoMapper wordResponseDtoMapper;

  @GetMapping
  public ResponseEntity<List<WordResponseDto>> getAllWords() {
    List<WordResponseDto> words = wordService.getAllWords().stream()
            .map(wordResponseDtoMapper)
            .toList();

    return ResponseEntity.ok(words);
  }
}