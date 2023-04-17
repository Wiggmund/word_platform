package com.example.wordPlatform.exception.notFound;

public class AttributeNotFoundException extends ResourceNotFoundException {
  public AttributeNotFoundException() {
    super("Attribute");
  }
}
