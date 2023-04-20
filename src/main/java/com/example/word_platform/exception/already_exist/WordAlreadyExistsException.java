package com.example.word_platform.exception.already_exist;

import java.util.List;

public class WordAlreadyExistsException extends ResourceAlreadyExistsException {
  private final List<String> duplicatedFields;

  public WordAlreadyExistsException(List<String> duplicatedFields) {
    super("Word");
    this.duplicatedFields = duplicatedFields;
  }

  @Override
  public List<String> getDuplicatedFields() {
    return duplicatedFields;
  }
}
