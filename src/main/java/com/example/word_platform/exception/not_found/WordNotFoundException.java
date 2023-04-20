package com.example.word_platform.exception.not_found;

public class WordNotFoundException extends ResourceNotFoundException {
  public WordNotFoundException() {
    super("Word");
  }
}
