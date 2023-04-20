package com.example.word_platform.exception.not_found;

public class WordlistNotFoundException extends ResourceNotFoundException {
  public WordlistNotFoundException() {
    super("Wordlist");
  }
}
