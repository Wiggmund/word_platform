package com.example.word_platform.exception.already_exist;

import java.util.List;

public class UserAlreadyExistsException extends ResourceAlreadyExistsException {
  private final List<String> duplicatedFields;

  public UserAlreadyExistsException(List<String> duplicatedFields) {
    super("User");
    this.duplicatedFields = duplicatedFields;
  }

  @Override
  public List<String> getDuplicatedFields() {
    return duplicatedFields;
  }
}
