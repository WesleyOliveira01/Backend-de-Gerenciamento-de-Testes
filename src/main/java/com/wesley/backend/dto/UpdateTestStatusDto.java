package com.wesley.backend.dto;

import com.wesley.backend.enums.TestEnum;

public record UpdateTestStatusDto(Long id, TestEnum status) {}
