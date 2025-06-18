package com.wesley.backend.exceptions;

import com.wesley.backend.dto.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ResponseDto<Object>> handleGeneral(Exception ex) {
    return ResponseEntity.badRequest().body(ResponseDto.error(ex.getMessage()));
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ResponseDto<Object>> handleNotFound(
    NotFoundException ex
  ) {
    return ResponseEntity.status(404).body(ResponseDto.error(ex.getMessage()));
  }

  @ExceptionHandler(TestModificationException.class)
  public ResponseEntity<ResponseDto<Object>> handleTestModification(
    TestModificationException ex
  ) {
    return ResponseEntity.status(400).body(ResponseDto.error(ex.getMessage()));
  }

  @ExceptionHandler(InvalidTestStatusException.class)
  public ResponseEntity<ResponseDto<Object>> handleInvalidTestStatus(
    InvalidTestStatusException ex
  ) {
    return ResponseEntity.status(400).body(ResponseDto.error(ex.getMessage()));
  }

  @ExceptionHandler(MissingTestIdOrNameException.class)
  public ResponseEntity<ResponseDto<Object>> handleIllegalArgument(
    MissingTestIdOrNameException ex
  ) {
    return ResponseEntity.status(400).body(ResponseDto.error(ex.getMessage()));
  }
}
