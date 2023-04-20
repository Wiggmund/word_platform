package com.example.word_platform.exception.not_found;

public class AttributeNotFoundException extends ResourceNotFoundException {
  public AttributeNotFoundException() {
    super("Attribute");
  }
}
