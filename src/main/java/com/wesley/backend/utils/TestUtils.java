package com.wesley.backend.utils;

import java.time.LocalDate;
import java.util.List;

import com.wesley.backend.enums.TestEnum;
import com.wesley.backend.exceptions.InvalidTestStatusException;
import com.wesley.backend.exceptions.NotFoundException;
import com.wesley.backend.models.Teste;

/**
 * Classe utilitária para operações relacionadas à entidade Test.
 * Contém métodos estáticos para validação, filtragem e cálculos de estatísticas.
 */
public class TestUtils {

  /**
   * Converte uma string para o enum TestEnum, lançando exceção se inválido.
   * @param status String representando o status.
   * @return TestEnum correspondente.
   * @throws InvalidTestStatusException se o status for inválido.
   */
  public static TestEnum parseStatus(String status) {
    try {
      return TestEnum.valueOf(status.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new InvalidTestStatusException(
        "Status de teste inválido: " + status
      );
    }
  }

  /**
   * Atualiza o status de todos os testes da lista conforme a lógica de negócio.
   * @param tests Lista de testes.
   */
  public static void updateStatusForTests(List<Teste> tests) {
    tests.forEach(Teste::updateStatus);
  }

  /**
   * Valida se a lista de testes não está vazia, lançando exceção se estiver.
   * @param tests Lista de testes.
   * @param status Status utilizado na busca.
   * @throws NotFoundException se a lista estiver vazia.
   */
  public static void validateTestsNotEmpty(List<Teste> tests, String status) {
    if (tests.isEmpty()) {
      throw new NotFoundException(
        "Nenhum teste encontrado com o status: " + status
      );
    }
  }

  /**
   * Filtra os testes realizados no mês atual, considerando a data de fim.
   * @param tests Lista de testes.
   * @return Lista de testes do mês atual.
   * @throws NotFoundException se a lista estiver vazia.
   */
  public static List<Teste> filterTestsByMonth(List<Teste> tests) {
    if (tests.isEmpty()) throw new NotFoundException("Nenhum teste encontrado");
    LocalDate now = LocalDate.now();
    return tests
      .stream()
      .filter(test -> test.getFim().getMonth() == now.getMonth())
      .toList();
  }

  /**
   * Conta quantos testes possuem o status informado.
   * @param tests Lista de testes.
   * @param status Status a ser contado.
   * @return Quantidade de testes com o status.
   */
  public static int countByStatus(List<Teste> tests, TestEnum status) {
    return (int) tests.stream()
      .filter(t -> t.getStatus() == status)
      .count();
  }

  /**
   * Calcula o percentual de uma parte em relação ao total.
   * @param parte Valor parcial.
   * @param total Valor total.
   * @return Percentual calculado, ou 0.0 se total for zero.
   */
  public static double percentual(int parte, int total) {
    return total > 0 ? (parte * 100.0) / total : 0.0;
  }
}