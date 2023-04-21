package com.example.word_platform.wordlist;

import com.example.word_platform.wordlist.dto.WordlistResponseDto;
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

  @GetMapping
  public ResponseEntity<List<WordlistResponseDto>> getAllWordlists() {
    List<WordlistResponseDto> wordlists = wordlistService.getAllWordlists().stream()
            .map(WordlistResponseDto::new)
            .toList();
    return ResponseEntity.ok(wordlists);
  }
}
