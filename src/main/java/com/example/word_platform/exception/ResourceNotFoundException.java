package com.example.word_platform.exception;

public class ResourceNotFoundException extends RuntimeException {
  private static final String RESOURCE_NOT_FOUND_MSG = "Entity not found";
  public ResourceNotFoundException(String message) {
    super(message.isEmpty() ? RESOURCE_NOT_FOUND_MSG : message);
  }
}
