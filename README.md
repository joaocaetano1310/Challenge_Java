# ☕ Java Web com Spring Framework

> Material de estudo e referência para a disciplina de Java Web com Spring.

---

## 📚 Conteúdo da Matéria

- [Visão Geral](#visão-geral)
- [Pré-requisitos](#pré-requisitos)
- [Tópicos Abordados](#tópicos-abordados)
- [Exemplos de Código](#exemplos-de-código)
- [Como Rodar os Projetos](#como-rodar-os-projetos)
- [Referências](#referências)

---

## Visão Geral

Esta disciplina aborda o desenvolvimento de aplicações web utilizando **Java** com o ecossistema **Spring**, cobrindo desde a criação de APIs REST até a persistência de dados com Spring Data JPA.

---

## Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- [JDK 17+](https://adoptium.net/)
- [Maven](https://maven.apache.org/) ou [Gradle](https://gradle.org/)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/) ou [VS Code](https://code.visualstudio.com/) com extensão Java
- [Postman](https://www.postman.com/) (para testar APIs)
- [Docker](https://www.docker.com/) *(opcional, para banco de dados)*

---

## Tópicos Abordados

### 1. 🌱 Introdução ao Spring Framework
- O que é o Spring e o ecossistema Spring Boot
- Inversão de Controle (IoC) e Injeção de Dependência (DI)
- Estrutura de um projeto Spring Boot
- Anotações essenciais: `@SpringBootApplication`, `@Component`, `@Service`, `@Repository`

### 2. 🌐 Spring MVC & REST
- Criação de Controllers com `@RestController`
- Mapeamento de rotas: `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`
- Parâmetros de rota e query: `@PathVariable`, `@RequestParam`
- Corpo da requisição: `@RequestBody`
- `ResponseEntity` e status HTTP

### 3. 🗄️ Spring Data JPA
- Configuração do banco de dados (H2, PostgreSQL, MySQL)
- Entidades com `@Entity`, `@Table`, `@Id`, `@GeneratedValue`
- Relacionamentos: `@OneToMany`, `@ManyToOne`, `@ManyToMany`
- Repositórios com `JpaRepository`
- Queries customizadas com `@Query` e JPQL

### 4. 🔒 Spring Security *(se aplicável)*
- Autenticação e autorização
- Configuração de segurança com `SecurityFilterChain`
- JWT (JSON Web Token)
- Roles e permissões

### 5. ✅ Validação e Tratamento de Erros
- Bean Validation: `@NotNull`, `@NotBlank`, `@Size`, `@Email`
- `@Valid` e `@Validated`
- `@ExceptionHandler` e `@ControllerAdvice`
- Respostas de erro padronizadas

### 6. 🧪 Testes
- Testes unitários com JUnit 5 e Mockito
- Testes de integração com `@SpringBootTest`
- Testes de controllers com `MockMvc`

---

## Exemplos de Código

### Controller REST básico

```java
@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    @GetMapping
    public ResponseEntity<List<Produto>> listarTodos() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Produto> criar(@RequestBody @Valid Produto produto) {
        Produto salvo = service.save(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id,
                                              @RequestBody @Valid Produto produto) {
        return ResponseEntity.ok(service.update(id, produto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
```

### Entidade JPA

```java
@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "Preço deve ser positivo")
    private BigDecimal preco;

    private String descricao;

    // Getters e Setters
}
```

### Repositório

```java
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByNomeContainingIgnoreCase(String nome);

    @Query("SELECT p FROM Produto p WHERE p.preco <= :precoMax")
    List<Produto> findByPrecoMaximo(@Param("precoMax") BigDecimal precoMax);
}
```

### Tratamento global de erros

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("erro", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> erros = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> erros.put(e.getField(), e.getDefaultMessage()));
        return ResponseEntity.badRequest().body(erros);
    }
}
```

---

## Como Rodar os Projetos

### 1. Clonar o repositório

```bash
git clone https://github.com/seu-usuario/seu-repositorio.git
cd seu-repositorio
```

### 2. Configurar o banco de dados

Edite o arquivo `src/main/resources/application.properties`:

```properties
# Banco H2 em memória (desenvolvimento)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.h2.console.enabled=true

# Ou PostgreSQL
# spring.datasource.url=jdbc:postgresql://localhost:5432/meudb
# spring.datasource.username=postgres
# spring.datasource.password=senha

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 3. Compilar e rodar

```bash
# Com Maven
./mvnw spring-boot:run

# Com Gradle
./gradlew bootRun
```

### 4. Acessar a aplicação

- API: `http://localhost:8080`
- Console H2: `http://localhost:8080/h2-console`
- Swagger (se configurado): `http://localhost:8080/swagger-ui.html`

---

## Referências

- 📖 [Documentação oficial do Spring](https://spring.io/docs)
- 📖 [Spring Boot Reference Guide](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- 📖 [Baeldung — tutoriais Spring](https://www.baeldung.com/)
- 📖 [Spring Initializr](https://start.spring.io/) — gerador de projetos
- 📖 [Jakarta EE Validation](https://beanvalidation.org/)
- 📖 [JPA / Hibernate Docs](https://hibernate.org/orm/documentation/)

---

## 🗂️ Estrutura Sugerida de Projeto

```
src/
├── main/
│   ├── java/com/exemplo/
│   │   ├── controller/    # Controllers REST
│   │   ├── service/       # Regras de negócio
│   │   ├── repository/    # Repositórios JPA
│   │   ├── model/         # Entidades
│   │   ├── dto/           # Data Transfer Objects
│   │   └── exception/     # Exceções customizadas
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/exemplo/  # Testes unitários e de integração
```

---

*Feito com ☕ e muito Spring*
