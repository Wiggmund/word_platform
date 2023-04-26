package com.example.word_platform.exception;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  private ApiExceptionResponse buildApiExceptionResponse(String message, HttpStatus status) {
    log.debug(message);
    return new ApiExceptionResponse(
        LocalDateTime.now(),
        status.value(),
        message
    );
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiExceptionResponse> handleResourceNotFoundException(
      ResourceNotFoundException ex) {
    HttpStatus status = HttpStatus.NOT_FOUND;
    ApiExceptionResponse response = buildApiExceptionResponse(ex.getMessage(), status);
    return ResponseEntity.status(status).body(response);
  }

  @ExceptionHandler(ResourceAlreadyExistsException.class)
  public ResponseEntity<ApiExceptionResponse> handleResourceAlreadyExistsException(
      ResourceAlreadyExistsException ex) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    ApiExceptionResponse response = buildApiExceptionResponse(ex.getMessage(), status);
    return ResponseEntity.status(status).body(response);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiExceptionResponse> handleIllegalArgumentException(
      IllegalArgumentException ex) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    ApiExceptionResponse response = buildApiExceptionResponse(ex.getMessage(), status);
    return ResponseEntity.status(status).body(response);
  }

  @ExceptionHandler(WordlistAttributesException.class)
  public ResponseEntity<ApiExceptionResponse> handleWordlistAttributesException(
      WordlistAttributesException ex) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    ApiExceptionResponse response = buildApiExceptionResponse(ex.getMessage(), status);
    return ResponseEntity.status(status).body(response);
  }
}
