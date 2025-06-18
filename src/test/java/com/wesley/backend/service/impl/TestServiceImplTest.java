package com.wesley.backend.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.wesley.backend.dto.TestDto;
import com.wesley.backend.dto.UpdateTestDto;
import com.wesley.backend.dto.UpdateTestStatusDto;
import com.wesley.backend.enums.TestEnum;
import com.wesley.backend.models.Teste;
import com.wesley.backend.repository.TestRepository;
import com.wesley.backend.service.TestService;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@SpringBootTest
@Transactional
public class TestServiceImplTest {

  @Autowired
  private TestService service;

  @Autowired
  private TestRepository repository;

  @Test
  void deveRetornarTodosOsTestes() {
    repository.save(new Teste(new TestDto(1L, "teste")));
    repository.save(new Teste(new TestDto(2L, "teste1")));

    Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
    Page<Teste> testes = service.findAll(pageable);

    assertEquals(testes.getContent().size(), 2);
    assertEquals(testes.getContent().get(0).getNome(), "teste");
    assertEquals(testes.getContent().get(1).getNome(), "teste1");
  }

  @Test
  void deveCriarUmNovoTeste() {
    TestDto novoTeste = new TestDto(1L, "Teste");

    Teste testeCriado = service.createTest(novoTeste);

    assertEquals(testeCriado.getId(), novoTeste.id());
    assertEquals(testeCriado.getNome(), novoTeste.nome());
  }

  @Test
  void deveAtualizarUmTeste() {
    repository.save(new Teste(new TestDto(1L, "teste")));

    Teste teste = service.updateTest(
      new UpdateTestDto(1L, "Teste atualizado", null)
    );

    assertEquals(1L, teste.getId());
    assertEquals("Teste atualizado", teste.getNome());
  }

  @Test
  void deveAtualizarStatusDeTeste() {
    repository.save(new Teste(new TestDto(1L, "teste")));

    Teste teste = service.updateTestStatus(
      new UpdateTestStatusDto(1L, TestEnum.ADQUIRIDO)
    );

    assertEquals(1L, teste.getId());
    assertEquals(TestEnum.ADQUIRIDO, teste.getStatus());
  }

  @Test
  void deveListarOsTestesPorStatus() {
    repository.save(new Teste(new TestDto(1L, "teste")));
    repository.save(new Teste(new TestDto(2L, "teste1")));
    repository.save(new Teste(new TestDto(3L, "teste2")));

    service.updateTestStatus(new UpdateTestStatusDto(1L, TestEnum.ADQUIRIDO));

    List<Teste> testes = service.findAllByStatus("EM_TESTE");

    assertEquals(2, testes.size());
    assertEquals("teste2", testes.get(1).getNome());
  }

  
}
