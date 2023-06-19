package com.example.word_platform.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  private ApiExceptionResponse buildApiExceptionResponse(Object message, HttpStatus status) {
    log.debug(message.toString());
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

  @ExceptionHandler(DatabaseRepositoryException.class)
  public ResponseEntity<ApiExceptionResponse> handleDatabaseRepositoryException(
      DatabaseRepositoryException ex) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    ApiExceptionResponse response = buildApiExceptionResponse(ex.getMessage(), status);
    return ResponseEntity.status(status).body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiExceptionResponse> handleDtoValidationException(MethodArgumentNotValidException ex) {
    List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
    HttpStatus status = HttpStatus.BAD_REQUEST;

    Map<String, List<String>> errors = new HashMap<>();
    for (FieldError fieldError: fieldErrors) {
      String fieldName = fieldError.getField();
      String errorMessage = fieldError.getDefaultMessage();

      errors.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(errorMessage);
    }

    ApiExceptionResponse response = buildApiExceptionResponse(errors, status);
    return ResponseEntity.status(status).body(response);
  }
}
