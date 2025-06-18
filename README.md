# Backend de Gerenciamento de Testes

Este projeto é uma API REST desenvolvida em Java com Spring Boot para gerenciamento de testes, permitindo cadastro, atualização, remoção, consulta e estatísticas de testes. O sistema utiliza boas práticas de arquitetura, DTOs, tratamento de exceções e paginação.

## Funcionalidades

- **Listagem de Testes:**  
  Consulte todos os testes cadastrados com suporte a paginação e ordenação.

- **Filtro por Status:**  
  Liste testes filtrando por status (`EM_TESTE`, `ADQUIRIDO`, `RETIRADO`, etc).

- **Estatísticas:**  
  Consulte estatísticas agregadas dos testes do mês atual.

- **Cadastro de Teste:**  
  Crie novos testes informando os dados necessários.

- **Atualização de Teste:**  
  Atualize dados de um teste existente.

- **Atualização de Status:**  
  Atualize o status de um teste para `ADQUIRIDO` ou `RETIRADO`.

- **Remoção de Teste:**  
  Remova um teste pelo seu ID.

## Endpoints

| Método | Endpoint                        | Descrição                                 |
|--------|---------------------------------|-------------------------------------------|
| GET    | `/v1/test`                      | Lista todos os testes (paginado)          |
| GET    | `/v1/test/status/{status}`      | Lista testes filtrando por status         |
| GET    | `/v1/test/statistics`           | Retorna estatísticas dos testes do mês    |
| POST   | `/v1/test`                      | Cria um novo teste                        |
| PUT    | `/v1/test/update`               | Atualiza um teste existente               |
| PUT    | `/v1/test/update-status`        | Atualiza o status de um teste             |
| DELETE | `/v1/test/delete/{id}`          | Remove um teste pelo ID                   |

## Estrutura do Projeto

- `controller/` — Camada de controle (REST Controllers)
- `service/` — Regras de negócio e serviços
- `models/` — Entidades JPA
- `dto/` — Objetos de transferência de dados
- `repository/` — Interfaces de persistência (Spring Data)
- `utils/` — Utilitários e helpers

## Exemplos de Uso

### Listar todos os testes (paginado)
```http
GET /v1/test?page=0&size=10&sort=id
```

### Filtrar por status
```http
GET /v1/test/status/EM_TESTE
```

### Criar um novo teste
```http
POST /v1/test
Content-Type: application/json

{
  "id": 1,
  "nome": "Cliente Teste"
}
```

### Atualizar um teste
```http
PUT /v1/test/update
Content-Type: application/json

{
  "id": 1,
  "nome": "Cliente Teste Atualizado"
}
```

### Atualizar status de um teste
```http
PUT /v1/test/update-status
Content-Type: application/json

{
  "id": 1,
  "status": "ADQUIRIDO"
}
```

### Remover um teste
```http
DELETE /v1/test/delete/1
```

## Requisitos

- Java 17 ou superior
- Maven
- Banco de dados relacional (H2, PostgreSQL, etc.)

## Como rodar

1. Clone o repositório:
   ```sh
   git clone https://github.com/WesleyOliveira01/Backend-de-Gerenciamento-de-Testes.git
   ```
2. Acesse a pasta do projeto:
   ```sh
   cd backend
   ```
3. Compile e execute:
   ```sh
   mvn spring-boot:run
   ```

A aplicação estará disponível em `http://localhost:8181`.

## Testes

Os testes unitários cobrem validações, regras de negócio e tratamento de exceções.  
Para rodar os testes:

```sh
mvn test
```

## Contribuição

Contribuições são bem-vindas!  
Abra uma issue ou envie um pull request.

---

**Autor:**