package com.example.word_platform.exception.already_exist;

import java.util.List;

public abstract class ResourceAlreadyExistsException extends RuntimeException {
  protected ResourceAlreadyExistsException(String entity) {
    super("The same " + entity + " alredy exists");
  }

  abstract List<String> getDuplicatedFields();
}
