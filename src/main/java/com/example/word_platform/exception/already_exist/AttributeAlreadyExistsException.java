package com.example.word_platform.exception.already_exist;

import java.util.List;

public class AttributeAlreadyExistsException extends ResourceAlreadyExistsException {
  private final List<String> duplicatedFields;

  public AttributeAlreadyExistsException(List<String> duplicatedFields) {
    super("Attribute");
    this.duplicatedFields = duplicatedFields;
  }

  @Override
  List<String> getDuplicatedFields() {
    return duplicatedFields;
  }
}
