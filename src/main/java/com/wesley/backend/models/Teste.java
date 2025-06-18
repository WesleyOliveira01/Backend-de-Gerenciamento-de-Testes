package com.wesley.backend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wesley.backend.dto.TestDto;
import com.wesley.backend.enums.TestEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Teste {

  @Id
  @Positive
  private Long id;

  private String nome;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @Enumerated(EnumType.STRING)
  private TestEnum status = TestEnum.EM_TESTE;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private LocalDate fim = LocalDate.now().plusDays(4);

  public Teste(TestDto test) {
    this.id = test.id();
    this.nome = test.nome();
  }

  public void updateStatus() {
    if (status == TestEnum.ADQUIRIDO || status == TestEnum.RETIRADO) {
      return;
    }
    LocalDate now = LocalDate.now();

    if (fim.isBefore(now)) {
      this.status = TestEnum.TESTE_EXPIRADO;
    } else if (fim.isEqual(now)) {
      this.status = TestEnum.TESTE_FINALIZADO;
    } else {
      this.status = TestEnum.EM_TESTE;
    }
  }
}
