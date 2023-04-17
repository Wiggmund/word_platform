package com.example.wordPlatform.exception.alreadyExist;

import java.util.List;

public class WordlistAlreadyExistsException extends ResourceAlreadyExistsException {
  private final List<String> duplicatedFields;

  public WordlistAlreadyExistsException(List<String> duplicatedFields) {
    super("Wordlist");
    this.duplicatedFields = duplicatedFields;
  }

  @Override
  public List<String> getDuplicatedFields() {
    return duplicatedFields;
  }
}
