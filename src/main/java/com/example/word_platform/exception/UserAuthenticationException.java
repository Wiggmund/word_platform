package com.example.word_platform.exception;

import org.springframework.security.core.AuthenticationException;

public class UserAuthenticationException extends AuthenticationException {
  public UserAuthenticationException(String msg) {
    super(msg);
  }
}
