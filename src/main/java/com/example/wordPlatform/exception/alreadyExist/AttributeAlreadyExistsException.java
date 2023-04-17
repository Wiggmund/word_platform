package com.example.wordPlatform.exception.alreadyExist;

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
