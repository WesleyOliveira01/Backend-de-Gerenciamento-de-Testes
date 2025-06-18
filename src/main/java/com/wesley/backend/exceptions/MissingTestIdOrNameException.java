package com.wesley.backend.exceptions;

public class MissingTestIdOrNameException extends RuntimeException {
  public MissingTestIdOrNameException(String message) {
    super(message);
  }
}

