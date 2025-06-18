package com.wesley.backend.dto;

public record EstatisticDto(
  Integer totalTestsByMonth,
  Integer acquired,
  Integer withdrawn,
  Double percentualAcquired,
  Double percentualWithdrawn,
  Double rateOfAcquired
) {}
