# ⚡ Subestação - Backend API

API REST para gerenciamento de subestações elétricas e suas respectivas redes de média tensão.

> Projeto desenvolvido com **Spring Boot 3**, **Spring Data JPA**, **Spring Security**, **MySQL**, e documentação via **Swagger (OpenAPI)**.

---

## 📚 Sumário

- [📌 Sobre o Projeto](#-sobre-o-projeto)
- [🚀 Tecnologias](#-tecnologias)
- [📁 Estrutura do Projeto](#-estrutura-do-projeto)
- [🔧 Como Rodar Localmente](#-como-rodar-localmente)
- [📄 Documentação da API](#-documentação-da-api)
- [🔐 Autenticação e Segurança](#-autenticação-e-segurança)
- [📦 Scripts SQL](#-scripts-sql)

---

## 📌 Sobre o Projeto

Esta API permite o cadastro, listagem, atualização e remoção de subestações elétricas e suas redes MT. A aplicação segue princípios RESTful e boas práticas de desenvolvimento com foco em modularidade, segurança e escalabilidade.

---

## 🚀 Tecnologias

- Java 17
- Spring Boot 3.2.4
- Spring Data JPA
- Spring Security
- MySQL
- Hibernate
- Lombok
- Swagger/OpenAPI (`springdoc-openapi`)
- Maven

---

## 📁 Estrutura do Projeto

```bash
src
├── main
│   ├── java/com/energia/subestacao
│   │   ├── config             # Configurações (Segurança, CORS, etc.)
│   │   ├── controller         # Controladores REST
│   │   ├── dto                # Data Transfer Objects
│   │   ├── model              # Entidades JPA
│   │   ├── repository         # Interfaces de repositório (JPA)
│   │   ├── service            # Lógica de negócios
│   │   └── SubestacaoApplication.java
│   └── resources
│       ├── application.properties
│       └── data.sql           # Dados de inicialização (mock)
```
## 🔧 Como Rodar Localmente
1. Pré-requisitos
   Java 17+

MySQL rodando localmente (porta padrão 3306)

Maven 3.8+

IDE (IntelliJ, VS Code, etc.)

2. Configurar o application.properties
   Crie um arquivo application.properties com a seguinte base:

- properties
```
spring.datasource.url=jdbc:mysql://localhost:3306/subestacao?createDatabaseIfNotExist=true&serverTimezone=UTC
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
springdoc.api-docs.path=/v3/api-docs
```
3. Rodar o projeto
```
   ./mvnw spring-boot:run
```
   Ou, se estiver com o MySQL rodando corretamente, rode direto pela IDE.

## 📄 Documentação da API
Após subir a aplicação, acesse:

http://localhost:8080/swagger-ui.html

Ou diretamente via:

http://localhost:8080/swagger-ui/index.html

## 🔐 Autenticação e Segurança
Por padrão, todas as rotas da API estão protegidas (HTTP 403). Para acessar rotas públicas (como Swagger):

Configurado via SecurityConfig.java

CORS e CSRF desabilitados apenas para desenvolvimento

## 📦 Scripts SQL
A aplicação já popula o banco automaticamente via data.sql com:

- Subestações de exemplo

- Redes MT associadas
