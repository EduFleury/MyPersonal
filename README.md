# 🏋️ Training API

API REST desenvolvida com **Java + Spring Boot** para gerenciamento de treinos entre **Personal Trainers** e seus alunos.

O sistema permite que o personal trainer crie e gerencie treinos, enquanto os alunos podem visualizar seus exercícios e registrar suas execuções, acompanhando a evolução dos treinamentos.

O projeto foi desenvolvido com foco em boas práticas de arquitetura, segurança e deploy, consolidando diversos conhecimentos exigidos em vagas de desenvolvimento Java Backend Pleno.

---

# 🚀 Tecnologias Utilizadas

* Java 21
* Spring Boot
* Spring Data JPA
* Spring Security
* JWT Authentication
* Refresh Token
* MySQL
* Maven
* Docker
* Docker Compose
* GitHub Actions
* Swagger / OpenAPI
* JUnit 5
* Mockito

---

# 🏛️ Arquitetura

O projeto segue o padrão arquitetural:

```text
Controller
   ↓
Service
   ↓
Repository
   ↓
Database
```

Estrutura das camadas:

```text
controller/
service/
repository/
dto/
model/
config/
exception/
security/
```

Principais conceitos aplicados:

✅ MVC (Model, View, Controller)

✅ DTO Pattern

✅ Repository Pattern

✅ Service Layer Pattern

✅ Tratamento Global de Exceções

✅ Autenticação e Autorização com JWT

✅ Refresh Token

✅ Paginação de APIs

✅ Testes Unitários

---

# 🔒 Segurança

A aplicação utiliza:

* Spring Security
* Autenticação baseada em JWT
* Refresh Token
* Senhas criptografadas com BCrypt
* Controle de acesso por perfis de usuário

Fluxo de autenticação:

```text
Login
   ↓
JWT Access Token
   ↓
Requisições autenticadas
   ↓
Expiração
   ↓
Refresh Token
   ↓
Novo Access Token
```

---

# 📚 Documentação da API

Após iniciar a aplicação:

http://localhost:8080/swagger-ui/index.html#/

Toda a documentação dos endpoints está disponível via Swagger/OpenAPI.

<img width="1897" height="870" alt="image" src="https://github.com/user-attachments/assets/73fdf0e9-18fa-458f-8eb1-34fc5a0c17ea" />

<img width="1891" height="867" alt="image" src="https://github.com/user-attachments/assets/f7db2d3e-2672-43d6-add2-2601ba84921d" />

---

# 🐳 Docker Hub

Imagem disponível em:

https://hub.docker.com/repository/docker/edufleury/training-api/general

Ou execute diretamente:

```bash
docker pull edufleury/training-api:latest
```
---

# 📦 Funcionalidades

## Personal Trainer

* Cadastro de alunos
* Criação de treinos
* Prescrição de exercícios
* Gerenciamento dos treinos dos alunos

## Aluno

* Visualização dos treinos recebidos
* Registro da execução dos exercícios
* Histórico de execuções

---

# 🗂️ Modelo de Dados

Entidades principais:

* Usuario
* PersonalTrainer
* Aluno
* Treino
* ItemTreino
* Exercicio
* ExecucaoTreino

---

# 🧪 Testes

O projeto possui:

✅ Testes unitários em 100% da camada Service.

Ferramentas utilizadas:

* JUnit 5
* Mockito

Os testes garantem a validação das regras de negócio e o comportamento esperado da aplicação.

---

# 📄 Paginação

Os principais endpoints GET implementam paginação utilizando:

```java
Pageable
Page<T>
PageRequest
```

Exemplo:

```http
GET /alunos?page=0&size=10
```

---

# 🐳 Docker

A aplicação pode ser executada integralmente via Docker, incluindo:

* API Spring Boot
* Banco de dados MySQL

## Executando com Docker Compose

### Clone o projeto

```bash
git clone https://github.com/edufleury/training-api
cd training-api
```

### Linux / Mac

```bash
cp .env.example .env
docker compose up -d
```

### Windows

```cmd
copy .env.example .env
docker compose up -d
```

---

# 🔐 Variáveis de Ambiente

O projeto possui um arquivo:

```text
.env.example
```

Basta copiá-lo para:

```text
.env
```

e a aplicação estará pronta para execução.

---

# 🔄 CI/CD

O projeto possui integração contínua utilizando GitHub Actions.

Pipeline:

```text
Push na branch principal
        ↓
Build do projeto
        ↓
Execução dos testes
        ↓
Build da imagem Docker
        ↓
Publicação automática no Docker Hub
```

---

# 🎯 Objetivos do Projeto

Este projeto foi desenvolvido com o objetivo de consolidar conhecimentos em:

* Arquitetura de aplicações Spring Boot
* Desenvolvimento de APIs REST
* Segurança com JWT
* Testes Unitários
* Dockerização
* Integração Contínua (CI/CD)
* Boas práticas de desenvolvimento Backend

---

# 👨‍💻 Autor

Eduardo Fleury

Desenvolvedor Backend Java.

GitHub:
https://github.com/edufleury

LinkedIn:
[https://www.linkedin.com/in/eduardo-fleury](https://www.linkedin.com/in/eduardo-pina-fleury-fortuna-51a57021b/)
