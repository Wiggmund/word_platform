package com.example.wordPlatform.wordlist.dto;

import com.example.wordPlatform.wordlist.WordlistEntity;

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
