# 🛒 E-Commerce REST API

API RESTful robusta desenvolvida para um ecossistema de comércio eletrónico (E-commerce), focada em boas práticas de engenharia de software, arquitetura em camadas, segurança stateless com JWT, integridade transacional de inventário e documentação interativa com OpenAPI/Swagger.

---

## 🛠️ Tecnologias e Ferramentas

- **Java 21** (LTS)
- **Spring Boot 4.x**
    - **Spring WebMVC** (Construção de endpoints REST)
    - **Spring Data JPA / Hibernate** (Mapeamento objeto-relacional e persistência)
    - **Spring Security** (Controlo de acessos e filtros de autenticação)
    - **Validation (Jakarta Bean Validation)** (Validação declarativa de DTOs)
- **PostgreSQL** (Base de dados relacional)
- **JJWT (Java JSON Web Token 0.11.5)** (Autenticação e geração de tokens stateless)
- **Springdoc OpenAPI 3 / Swagger UI** (Documentação viva e interativa da API)
- **JUnit 5 & Mockito** (Testes unitários e de integração de segurança)
- **Maven** (Gestão de dependências e build)

---

## 🏛️ Arquitetura e Estrutura do Projeto

O projeto segue a separação estrita de responsabilidades em camadas limpas, utilizando **Records do Java** como DTOs imutáveis para transferência de dados:

```text
src/main/java/com/adrielbarros/ecommerce/
├── config/             # Configurações globais (OpenAPI/Swagger, Beans do Spring)
├── controller/         # Controladores REST expondo os endpoints HTTP
├── dto/                # Data Transfer Objects imutáveis (Records)
├── exception/          # Tratamento global de exceções (@RestControllerAdvice)
├── model/              # Entidades JPA (Usuario, Produto, Pedido, ItemPedido, Enums)
├── repository/         # Interfaces Spring Data JPA para comunicação com a BD
├── security/           # Filtros JWT, UserDetailsService e SecurityFilterChain
└── service/            # Regras de negócio, cálculos e gestão transacional (@Transactional)