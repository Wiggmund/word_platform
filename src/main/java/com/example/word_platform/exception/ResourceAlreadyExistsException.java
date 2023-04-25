package com.example.word_platform.exception;

public class ResourceAlreadyExistsException extends RuntimeException {
  private static final String RESOURCE_ALREADY_EXISTS_MSG = "Entity already exists";

  public ResourceAlreadyExistsException(String message) {
    super(message.isEmpty() ? RESOURCE_ALREADY_EXISTS_MSG : message);
  }
}
