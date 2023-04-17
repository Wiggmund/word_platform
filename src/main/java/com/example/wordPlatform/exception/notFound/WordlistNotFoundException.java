package com.example.wordPlatform.exception.notFound;

public class WordlistNotFoundException extends ResourceNotFoundException {
  public WordlistNotFoundException() {
    super("Wordlist");
  }
}
