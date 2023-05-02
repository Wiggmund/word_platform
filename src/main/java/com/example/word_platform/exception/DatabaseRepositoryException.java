package com.example.word_platform.exception;

public class DatabaseRepositoryException extends RuntimeException {
  private static final String REPOSITORY_EXCEPTION = "Repoditory exception";
  public DatabaseRepositoryException(String message) {
    super(message.isEmpty() ? REPOSITORY_EXCEPTION : message);
  }
}
