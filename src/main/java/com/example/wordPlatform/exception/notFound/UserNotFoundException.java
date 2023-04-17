package com.example.wordPlatform.exception.notFound;

public class UserNotFoundException extends ResourceNotFoundException {
  public UserNotFoundException() {
    super("User");
  }
}
