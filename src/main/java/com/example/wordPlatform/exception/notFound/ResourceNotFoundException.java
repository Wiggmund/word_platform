package com.example.wordPlatform.exception.notFound;

public abstract class ResourceNotFoundException extends RuntimeException {
  protected ResourceNotFoundException(String entity) {
    super(entity + " not found");
  }
}
