package com.wesley.backend.dto;

import java.util.List;

public record ResponsePageDto<T>(
  Boolean ok,
  String error,
  List<T> data,
  int page,
  int size,
  long totalElements,
  int totalPages
) {
  public static <T> ResponsePageDto<T> success(
    org.springframework.data.domain.Page<T> pageData
  ) {
    return new ResponsePageDto<>(
      true,
      null,
      pageData.getContent(),
      pageData.getNumber(),
      pageData.getSize(),
      pageData.getTotalElements(),
      pageData.getTotalPages()
    );
  }

  public static <T> ResponsePageDto<T> error(String errorMessage) {
    return new ResponsePageDto<>(false, errorMessage, null, 0, 0, 0, 0);
  }
}
