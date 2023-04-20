package com.example.wordPlatform.exception.notFound;

public class WordNotFoundException extends ResourceNotFoundException {
  public WordNotFoundException() {
    super("Word");
  }
}
