package com.example.word_platform.exception;

import java.util.List;

public class IllegalAttributesException extends RuntimeException {
  private final List<String> attributeNames;

  public IllegalAttributesException(String message, List<String> attributeNames) {
    super(message);
    this.attributeNames = attributeNames;
  }

  public List<String> getAttributeNames() {
    return attributeNames;
  }
}
