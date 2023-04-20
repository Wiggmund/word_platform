package com.example.word_platform.wordlist.dto;

import com.example.word_platform.wordlist.WordlistEntity;

public record WordlistResponseDto(
        Long id,
        String title,
        String description,
        Long userId
) {
  public WordlistResponseDto(WordlistEntity wordlist) {
    this(
            wordlist.getId(),
            wordlist.getTitle(),
            wordlist.getDescription(),
            wordlist.getUser().getId()
    );
  }
}
