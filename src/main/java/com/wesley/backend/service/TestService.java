package com.wesley.backend.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wesley.backend.dto.EstatisticDto;
import com.wesley.backend.dto.TestDto;
import com.wesley.backend.dto.UpdateTestDto;
import com.wesley.backend.dto.UpdateTestStatusDto;
import com.wesley.backend.models.Teste;

public interface TestService {
  Page<Teste> findAll(Pageable pageable);
  List<Teste> findAllByStatus(String status);
  Teste createTest(TestDto test);
  Teste updateTest(UpdateTestDto test);
  Teste updateTestStatus(UpdateTestStatusDto testStatusDto);
  void deleteTest(Long id);
  EstatisticDto getStatistics();
}
