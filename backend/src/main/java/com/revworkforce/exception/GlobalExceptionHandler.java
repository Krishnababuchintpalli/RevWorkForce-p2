package com.revworkforce.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {

    log.error("Resource not found exception: {}", ex.getMessage());

    ErrorResponse error = new ErrorResponse(
      LocalDateTime.now(),
      HttpStatus.NOT_FOUND.value(),
      "Resource Not Found",
      ex.getMessage()
    );

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {

    log.error("Unhandled exception occurred: {}", ex.getMessage());

    ErrorResponse error = new ErrorResponse(
      LocalDateTime.now(),
      HttpStatus.INTERNAL_SERVER_ERROR.value(),
      "Internal Server Error",
      ex.getMessage()
    );

    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
