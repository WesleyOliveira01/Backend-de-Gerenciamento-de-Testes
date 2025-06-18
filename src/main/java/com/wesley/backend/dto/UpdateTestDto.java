package com.wesley.backend.dto;

import java.time.LocalDate;

public record UpdateTestDto(Long id, String nome, LocalDate fim) {}
