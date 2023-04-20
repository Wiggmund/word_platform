package com.example.word_platform.exception.not_found;

public abstract class ResourceNotFoundException extends RuntimeException {
  protected ResourceNotFoundException(String entity) {
    super(entity + " not found");
  }
}
