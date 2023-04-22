package com.example.word_platform.dto.wordlist;

import com.example.word_platform.model.Wordlist;

public record WordlistResponseDto(
        Long id,
        String title,
        String description,
        Long userId
) {
  public WordlistResponseDto(Wordlist wordlist) {
    this(
            wordlist.getId(),
            wordlist.getTitle(),
            wordlist.getDescription(),
            wordlist.getUser().getId()
    );
  }
}
