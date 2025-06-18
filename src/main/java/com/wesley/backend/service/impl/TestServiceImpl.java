package com.wesley.backend.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wesley.backend.dto.EstatisticDto;
import com.wesley.backend.dto.TestDto;
import com.wesley.backend.dto.UpdateTestDto;
import com.wesley.backend.dto.UpdateTestStatusDto;
import com.wesley.backend.enums.TestEnum;
import com.wesley.backend.exceptions.InvalidTestStatusException;
import com.wesley.backend.exceptions.MissingTestIdOrNameException;
import com.wesley.backend.exceptions.NotFoundException;
import com.wesley.backend.exceptions.TestModificationException;
import com.wesley.backend.models.Teste;
import com.wesley.backend.repository.TestRepository;
import com.wesley.backend.service.TestService;
import com.wesley.backend.utils.TestUtils;

@Service
public class TestServiceImpl implements TestService {

  @Autowired
  private TestRepository testRepository;

  /**
   * Retorna todos os testes, atualizando o status de cada um.
   */
  @Override
  public Page<Teste> findAll(Pageable pageable) {
    Page<Teste> tests = testRepository.findAll(pageable);
    tests.forEach(Teste::updateStatus);
    return tests;
  }

  /**
   * Cria um novo teste, validando campos obrigatórios e atualizando status.
   */
  @Override
  @Transactional
  public Teste createTest(TestDto test) {
    if (test.id() == null || test.nome() == null) {
      throw new MissingTestIdOrNameException(
        "ID e nome do teste são obrigatórios."
      );
    }
    Teste newTest = new Teste(test);
    return saveAndUpdateStatus(newTest);
  }

  /**
   * Atualiza um teste existente, exceto se já estiver adquirido ou retirado.
   */
  @Override
  @Transactional
  public Teste updateTest(UpdateTestDto testDto) {
    Teste test = getTestById(testDto.id());

    if (
      test.getStatus() == TestEnum.ADQUIRIDO ||
      test.getStatus() == TestEnum.RETIRADO
    ) {
      throw new TestModificationException(
        "Teste já adquirido ou retirado, não é possível atualizar."
      );
    }

    if (testDto.nome() != null) {
      test.setNome(testDto.nome());
    }
    if (testDto.fim() != null) {
      test.setFim(testDto.fim());
    }

    return saveAndUpdateStatus(test);
  }

  /**
   * Atualiza o status do teste para ADQUIRIDO ou RETIRADO.
   */
  @Override
  @Transactional
  public Teste updateTestStatus(UpdateTestStatusDto testDto) {
    Teste test = getTestById(testDto.id());

    if (
      testDto.status() != TestEnum.ADQUIRIDO &&
      testDto.status() != TestEnum.RETIRADO
    ) {
      throw new InvalidTestStatusException(
        "O status deve ser ADQUIRIDO ou RETIRADO para atualizar o teste."
      );
    }

    test.setStatus(testDto.status());
    return testRepository.save(test);
  }

  /**
   * Remove um teste pelo ID.
   */
  @Override
  @Transactional
  public void deleteTest(Long id) {
    Teste test = getTestById(id);
    testRepository.delete(test);
  }

  /**
   * Busca todos os testes por status, atualizando o status de cada um.
   */
  @Override
  public List<Teste> findAllByStatus(String status) {
    TestEnum testStatus = TestUtils.parseStatus(status);
    List<Teste> tests = testRepository.findByStatus(testStatus);
    TestUtils.updateStatusForTests(tests);
    TestUtils.validateTestsNotEmpty(tests, status);
    return List.copyOf(tests);
  }

  /**
   * Retorna estatísticas dos testes do mês atual.
   */
  @Override
  public EstatisticDto getStatistics() {
    List<Teste> allTests = testRepository.findAll();
    List<Teste> testsByMonth = TestUtils.filterTestsByMonth(allTests);

    int totalTestsByMonth = testsByMonth.size();
    int acquired = TestUtils.countByStatus(testsByMonth, TestEnum.ADQUIRIDO);
    int withdrawn = TestUtils.countByStatus(testsByMonth, TestEnum.RETIRADO);

    double percentualAcquired = TestUtils.percentual(
      acquired,
      totalTestsByMonth
    );
    double percentualWithdrawn = TestUtils.percentual(
      withdrawn,
      totalTestsByMonth
    );
    double taxaAdesao = percentualAcquired;

    return new EstatisticDto(
      totalTestsByMonth,
      acquired,
      withdrawn,
      percentualAcquired,
      percentualWithdrawn,
      taxaAdesao
    );
  }

  // Métodos utilitários privados

  private Teste getTestById(Long id) {
    return testRepository
      .findById(id)
      .orElseThrow(() -> new NotFoundException("Teste não encontrado"));
  }

  private Teste saveAndUpdateStatus(Teste test) {
    test.updateStatus();
    return testRepository.save(test);
  }
}
