package com.example.word_platform.exception.not_found;

public class UserNotFoundException extends ResourceNotFoundException {
  public UserNotFoundException() {
    super("User");
  }
}
