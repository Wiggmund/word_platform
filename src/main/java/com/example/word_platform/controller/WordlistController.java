package com.example.word_platform.controller;

import com.example.word_platform.service.WordlistService;
import com.example.word_platform.dto.wordlist.WordlistResponseDto;
import com.example.word_platform.shared.EntityConverter;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/wordlists")
@AllArgsConstructor
public class WordlistController {
  private final WordlistService wordlistService;
  private final EntityConverter entityConverter;

  @GetMapping
  public ResponseEntity<List<WordlistResponseDto>> getAllWordlists() {
    List<WordlistResponseDto> wordlists = wordlistService.getAllWordlists().stream()
            .map(entityConverter::entityToDto)
            .toList();
    return ResponseEntity.ok(wordlists);
  }
}
