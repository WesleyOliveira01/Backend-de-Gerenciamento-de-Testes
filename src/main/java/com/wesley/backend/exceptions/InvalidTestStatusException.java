package com.wesley.backend.exceptions;

public class InvalidTestStatusException extends RuntimeException {
  public InvalidTestStatusException(String message) {
    super(message);
  }
}
