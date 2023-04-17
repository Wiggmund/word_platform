package com.example.wordPlatform.exception.alreadyExist;

import java.util.List;

public abstract class ResourceAlreadyExistsException extends RuntimeException {
  protected ResourceAlreadyExistsException(String entity) {
    super("The same " + entity + " alredy exists");
  }

  abstract List<String> getDuplicatedFields();
}
