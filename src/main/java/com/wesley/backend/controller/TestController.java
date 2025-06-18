package com.wesley.backend.controller;

import com.wesley.backend.dto.EstatisticDto;
import com.wesley.backend.dto.ResponseDto;
import com.wesley.backend.dto.TestDto;
import com.wesley.backend.dto.UpdateTestDto;
import com.wesley.backend.dto.UpdateTestStatusDto;
import com.wesley.backend.models.Teste;
import com.wesley.backend.service.TestService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wesley.backend.dto.ResponsePageDto;

@RestController
@RequestMapping("/v1/test")
public class TestController {

  @Autowired
  private TestService testService;

  /**
   * Retorna todos os testes cadastrados.
   * @return Lista de testes.
   */
  @GetMapping
  public ResponseEntity<ResponsePageDto<Teste>> getAllTests(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size,
    @RequestParam(defaultValue = "id") String sort
  ) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
    Page<Teste> tests = testService.findAll(pageable);
    return ResponseEntity.ok(ResponsePageDto.success(tests));
  }

  /**
   * Retorna testes filtrados por status.
   * @param status Status do teste (ex: EM_TESTE, ADQUIRIDO, RETIRADO).
   * @return Lista de testes com o status informado.
   */
  @GetMapping("status/{status}")
  public ResponseEntity<ResponseDto<Teste>> getTestsByStatus(
    @PathVariable String status
  ) {
    List<Teste> tests = testService.findAllByStatus(status);
    return ResponseEntity.ok(ResponseDto.success(tests));
  }

  /**
   * Retorna estatísticas dos testes do mês atual.
   * @return Estatísticas agregadas dos testes do mês.
   */
  @GetMapping("/statistics")
  public ResponseEntity<ResponseDto<EstatisticDto>> getStatistics() {
    EstatisticDto estatisticDto = testService.getStatistics();
    return ResponseEntity.ok(ResponseDto.success(estatisticDto));
  }

  /**
   * Cria um novo teste.
   * @param test Dados do teste a ser criado.
   * @return Teste criado.
   */
  @PostMapping
  public ResponseEntity<ResponseDto<Teste>> createTest(
    @RequestBody @Valid TestDto test
  ) {
    Teste createdTest = testService.createTest(test);
    return ResponseEntity.ok(ResponseDto.success(List.of(createdTest)));
  }

  /**
   * Atualiza um teste existente.
   * @param testDto Dados para atualização do teste.
   * @return Teste atualizado.
   */
  @PutMapping("update")
  public ResponseEntity<ResponseDto<Teste>> updateTest(
    @RequestBody UpdateTestDto testDto
  ) {
    Teste updatedTest = testService.updateTest(testDto);
    return ResponseEntity.ok(ResponseDto.success(List.of(updatedTest)));
  }

  /**
   * Atualiza o status de um teste para ADQUIRIDO ou RETIRADO.
   * @param testDto Dados para atualização do status do teste.
   * @return Teste com status atualizado.
   */
  @PutMapping("update-status")
  public ResponseEntity<ResponseDto<Teste>> updateTestStatus(
    @RequestBody UpdateTestStatusDto testDto
  ) {
    Teste updatedTest = testService.updateTestStatus(testDto);
    return ResponseEntity.ok(ResponseDto.success(List.of(updatedTest)));
  }

  /**
   * Remove um teste pelo ID.
   * @param id Identificador do teste a ser removido.
   * @return Confirmação da remoção.
   */
  @DeleteMapping("delete/{id}")
  public ResponseEntity<ResponseDto<Void>> deleteTest(@PathVariable Long id) {
    testService.deleteTest(id);
    return ResponseEntity.ok(ResponseDto.success(null));
  }
}
