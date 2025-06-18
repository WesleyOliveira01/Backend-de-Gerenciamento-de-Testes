package com.wesley.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wesley.backend.enums.TestEnum;
import com.wesley.backend.models.Teste;

public interface TestRepository extends JpaRepository<Teste, Long> {
  List<Teste> findByStatus(TestEnum status);
}
