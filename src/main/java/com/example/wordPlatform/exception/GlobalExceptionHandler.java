package com.example.wordPlatform.exception;

import com.example.wordPlatform.exception.alreadyExist.ResourceAlreadyExistsException;
import com.example.wordPlatform.exception.notFound.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiExceptionResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
    HttpStatus status = HttpStatus.NOT_FOUND;
    ApiExceptionResponse response = new ApiExceptionResponse(
            LocalDateTime.now(),
            status.value(),
            ex.getMessage()
    );

    return ResponseEntity.status(status).body(response);
  }

  @ExceptionHandler(ResourceAlreadyExistsException.class)
  public ResponseEntity<ApiExceptionResponse> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    ApiExceptionResponse response = new ApiExceptionResponse(
            LocalDateTime.now(),
            status.value(),
            ex.getMessage()
    );

    return ResponseEntity.status(status).body(response);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiExceptionResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    ApiExceptionResponse response = new ApiExceptionResponse(
            LocalDateTime.now(),
            status.value(),
            ex.getMessage()
    );

    return ResponseEntity.status(status).body(response);
  }

  @ExceptionHandler(IllegalAttributesException.class)
  public ResponseEntity<ApiExceptionResponse> handleIllegalAttributesException(IllegalAttributesException ex) {
    HttpStatus status = HttpStatus.BAD_REQUEST;

    Map<String, Object> body = new HashMap<>();
    body.put("message", ex.getMessage());
    body.put("attributes", ex.getAttributeNames());

    ApiExceptionResponse response = new ApiExceptionResponse(
            LocalDateTime.now(),
            status.value(),
            body
    );

    return ResponseEntity.status(status).body(response);
  }
}
