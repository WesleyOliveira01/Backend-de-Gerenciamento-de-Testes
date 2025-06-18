package com.wesley.backend.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.wesley.backend.dto.TestDto;
import com.wesley.backend.enums.TestEnum;
import com.wesley.backend.models.Teste;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class TestRepositoryTest {

  @Autowired
  private TestRepository repository;

  @Test
  void deveListarTodosOsTestes() {
    repository.save(new Teste(new TestDto(1L, "teste")));
    repository.save(new Teste(new TestDto(2L, "teste1")));
    repository.save(new Teste(new TestDto(3L, "teste2")));

    List<Teste> testes = repository.findAll();

    assertThat(testes.size()).isEqualTo(3);
    assertThat(testes.get(1).getNome()).isEqualTo("teste1");
  }

  @Test
  void deveCriarUmNovoTeste() {
    Teste teste = repository.save(new Teste(new TestDto(1L, "teste")));

    assertThat(teste.getId()).isEqualTo(1L);
    assertThat(teste.getNome()).isEqualTo("teste");
    assertThat(teste.getStatus()).isEqualTo(TestEnum.EM_TESTE);
  }

  @Test
  void deveBuscarUmTestePeloId() {
    repository.save(new Teste(new TestDto(1L, "teste")));
    Optional<Teste> testeEncontrado = repository.findById(1L);

    assertThat(testeEncontrado.get().getId()).isEqualTo(1L);
    assertThat(testeEncontrado.get().getNome()).isEqualTo("teste");
    assertThat(testeEncontrado.get().getStatus()).isEqualTo(TestEnum.EM_TESTE);
  }

  @Test
  void deveListarTestesPeloStatus() {
    Teste teste = repository.save(new Teste(new TestDto(1L, "teste")));
    repository.save(new Teste(new TestDto(2L, "teste1")));
    repository.save(new Teste(new TestDto(3L, "teste2")));

    teste.setStatus(TestEnum.RETIRADO);

    List<Teste> testesEncontrados = repository.findByStatus(TestEnum.EM_TESTE);

    assertThat(testesEncontrados.size()).isEqualTo(2);
    assertThat(testesEncontrados.get(0).getNome()).isEqualTo("teste1");
    assertThat(testesEncontrados.get(1).getNome()).isEqualTo("teste2");
  }
}
