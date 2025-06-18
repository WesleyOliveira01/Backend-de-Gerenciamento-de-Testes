package com.wesley.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wesley.backend.dto.TestDto;
import com.wesley.backend.dto.UpdateTestDto;
import com.wesley.backend.dto.UpdateTestStatusDto;
import com.wesley.backend.enums.TestEnum;
import com.wesley.backend.models.Teste;
import com.wesley.backend.repository.TestRepository;
import com.wesley.backend.service.TestService;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TestControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private TestRepository repository;

  @Autowired
  private TestService service;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void deveListarTodosOsTestes() throws Exception {
    repository.save(new Teste(new TestDto(1L, "Cliente teste")));
    repository.save(new Teste(new TestDto(2L, "Cliente teste2")));

    mockMvc
      .perform(get("/v1/test"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.data[0].id").value(1))
      .andExpect(jsonPath("$.data[0].nome").value("Cliente teste"))
      .andExpect(jsonPath("$.data[1].id").value(2))
      .andExpect(jsonPath("$.data[1].nome").value("Cliente teste2"));
  }

  @Test
  void deveListarTodosOsTestesPeloStatus() throws Exception {
    Teste teste = repository.save(new Teste(new TestDto(1L, "Cliente teste")));
    repository.save(new Teste(new TestDto(2L, "Cliente teste2")));
    repository.save(new Teste(new TestDto(3L, "Cliente teste3")));

    service.updateTestStatus(
      new UpdateTestStatusDto(teste.getId(), TestEnum.ADQUIRIDO)
    );

    mockMvc
      .perform(get("/v1/test/status/EM_TESTE"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.data[0].nome").value("Cliente teste2"))
      .andExpect(jsonPath("$.data[1].nome").value("Cliente teste3"));
  }

  @Test
  void deveRetornarAsEstatisticas() throws Exception {
    Teste teste = repository.save(new Teste(new TestDto(1L, "Teste")));
    Teste teste1 = repository.save(new Teste(new TestDto(2L, "Teste1")));

    service.updateTestStatus(
      new UpdateTestStatusDto(teste.getId(), TestEnum.ADQUIRIDO)
    );
    service.updateTestStatus(
      new UpdateTestStatusDto(teste1.getId(), TestEnum.RETIRADO)
    );

    mockMvc
      .perform(get("/v1/test/statistics"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.data[0].totalTestsByMonth").value(2))
      .andExpect(jsonPath("$.data[0].acquired").value(1))
      .andExpect(jsonPath("$.data[0].withdrawn").value(1))
      .andExpect(jsonPath("$.data[0].percentualAcquired").value(50))
      .andExpect(jsonPath("$.data[0].percentualWithdrawn").value(50))
      .andExpect(jsonPath("$.data[0].rateOfAcquired").value(50));
  }

  @Test
  void deveCriarUmNovoTeste() throws Exception {
    TestDto teste = new TestDto(1L, "teste");

    mockMvc
      .perform(
        post("/v1/test")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(teste))
      )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.data[0].id").value(1))
      .andExpect(jsonPath("$.data[0].nome").value("teste"))
      .andExpect(jsonPath("$.data[0].status").value("EM_TESTE"))
      .andExpect(jsonPath("$.data[0].fim").value("2025-06-21"));
  }

  @Test
  void deveAtualizarUmTeste() throws Exception {
    repository.save(new Teste(new TestDto(1L, "Teste")));
    UpdateTestDto testeAtualizado = new UpdateTestDto(
      1L,
      "Teste Atualizado",
      null
    );

    mockMvc
      .perform(
        put("/v1/test/update")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(testeAtualizado))
      )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.data[0].nome").value("Teste Atualizado"));
  }

  @Test
  void deveAtualizarOstatusDeUmTeste() throws Exception {
    repository.save(new Teste(new TestDto(1L, "teste")));
    UpdateTestStatusDto updateTestStatusDto = new UpdateTestStatusDto(
      1L,
      TestEnum.ADQUIRIDO
    );

    mockMvc
      .perform(
        put("/v1/test/update-status")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(updateTestStatusDto))
      )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.data[0].status").value("ADQUIRIDO"));
  }

  @Test
  void deveDeletarUmTeste() throws Exception {
    Teste teste = repository.save(new Teste(new TestDto(1L, "Teste")));

    mockMvc
      .perform(delete("/v1/test/delete/" + teste.getId()))
      .andExpect(status().isOk());
  }

  @Test
  void deveRetornarBadRequestCasoIdOuNomeSejaNulo() throws Exception {
    TestDto teste = new TestDto(null, "teste");

    mockMvc
      .perform(
        post("/v1/test")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(teste))
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        jsonPath("$.error").value("ID e nome do teste são obrigatórios.")
      );
  }

  @Test
  void deveLancarTestModificationExceptionQuandoStatusForInvalido()
    throws Exception {
    repository.save(new Teste(new TestDto(1L, "teste")));
    UpdateTestStatusDto updateTestStatusDto = new UpdateTestStatusDto(
      1L,
      TestEnum.ADQUIRIDO
    );

    service.updateTestStatus(updateTestStatusDto);

    UpdateTestDto updateTestDto = new UpdateTestDto(1L, null, null);

    mockMvc
      .perform(
        put("/v1/test/update")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(updateTestDto))
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        jsonPath("$.error").value(
          "Teste já adquirido ou retirado, não é possível atualizar."
        )
      );
  }

  @Test
  void deveLancarInvalidTestStatusExceptionQuandoStatusForInvalido() throws Exception {
    repository.save(new Teste(new TestDto(1L, "teste")));
    UpdateTestStatusDto updateTestStatusDto = new UpdateTestStatusDto(
      1L,
      TestEnum.TESTE_FINALIZADO
    );

    mockMvc
      .perform(
        put("/v1/test/update-status")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(updateTestStatusDto))
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        jsonPath("$.error").value(
          "O status deve ser ADQUIRIDO ou RETIRADO para atualizar o teste."
        )
      );
  }
}
