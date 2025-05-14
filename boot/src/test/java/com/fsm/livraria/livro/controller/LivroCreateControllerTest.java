package com.fsm.livraria.livro.controller;

import com.fsm.exceptions.exception.FieldMessage;
import com.fsm.exceptions.exception.ValidateError;
import com.fsm.livraria.autor.dto.AutorCreatedRequest;
import com.fsm.livraria.categoria.controller.CategoriaController;
import com.fsm.livraria.categoria.dto.CategoriaCreatedRequest;
import com.fsm.livraria.livro.dto.LivroCreatedRequest;
import com.fsm.utils.AutenticationUtils;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import jakarta.validation.constraints.NotNull;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@MicronautTest
public class LivroCreateControllerTest {

    private UUID autorGlobal;

    private UUID categoriaGlobal;

    @Test
    void testCreateLivroSuccess(RequestSpecification spec) {
        // Obter token
        String token = new AutenticationUtils(spec).getToken();

        // Criar categoria
        UUID getCategoryId = createCategory(spec, token);

        // Criar autor
        UUID getAutorId = createAutor(spec, token);

        // Criar livro
        LivroCreatedRequest livroRequest = createValidLivroRequest(getAutorId, getCategoryId);

        // Enviar requisição
        spec.given().contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(livroRequest)
                .when()
                .post(LivroCreateController.LIVRO_CREATED)
                .then()
                .statusCode(201);
    }

    @Test
    void testEmptygetTitleValidation(RequestSpecification spec) {
        // Obter token
        String token = new AutenticationUtils(spec).getToken();

        // Criar categoria e autor
        UUID getCategoryId = createCategory(spec, token);
        UUID getAutorId = createAutor(spec, token);

        // Criar livro com título vazio
        LivroCreatedRequest livroRequest = createValidLivroRequest(getAutorId, getCategoryId);
        livroRequest = new LivroCreatedRequest(
                "", // título vazio
                livroRequest.getResume(),
                livroRequest.getSummary(),
                livroRequest.getIsbn(),
                livroRequest.getNumberPages(),
                livroRequest.getPublicationDate(),
                livroRequest.getPrice(),
                livroRequest.getAutorId(),
                livroRequest.getCategoryId()
        );

        // Verificar erro 422
        ValidateError validateError = spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(livroRequest)
                .when()
                .post(LivroCreateController.LIVRO_CREATED)
                .then()
                .statusCode(422)
                .extract()
                .as(ValidateError.class);

        assertValidationError(validateError, "O título é obrigatório");
    }

    @Test
    void testUniquegetTitleValidation(RequestSpecification spec) {
        // Obter token
        String token = new AutenticationUtils(spec).getToken();

        // Criar categoria e autor
        UUID getCategoryId = createCategory(spec, token);
        UUID getAutorId = createAutor(spec, token);

        // Criar livro válido
        LivroCreatedRequest livroRequest = createValidLivroRequest(getAutorId, getCategoryId);

        // Primeira requisição (deve ser bem-sucedida)
        Response post = spec.given().contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(livroRequest)
                .when()
                .post(LivroCreateController.LIVRO_CREATED);

        JsonPath jsonPath = post.jsonPath();

        // Verificar status 201
        post.then()
                .statusCode(HttpStatus.CREATED.getCode());

        // Segunda requisição com mesmo título (deve falhar)
        ValidateError validateError = spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(livroRequest)
                .when()
                .post(LivroCreateController.LIVRO_CREATED)
                .then()
                .statusCode(422)
                .extract()
                .as(ValidateError.class);

        assertValidationError(validateError, "título já está em uso");
    }

    @Test
    void testEmptygetResumeValidation(RequestSpecification spec) {
        // Obter token
        String token = new AutenticationUtils(spec).getToken();

        // Criar categoria e autor
        UUID getCategoryId = createCategory(spec, token);
        UUID getAutorId = createAutor(spec, token);

        // Criar livro com resumo vazio
        LivroCreatedRequest livroRequest = createValidLivroRequest(getAutorId, getCategoryId);
        livroRequest = new LivroCreatedRequest(
                livroRequest.getTitle(),
                "", // resumo vazio
                livroRequest.getSummary(),
                livroRequest.getIsbn(),
                livroRequest.getNumberPages(),
                livroRequest.getPublicationDate(),
                livroRequest.getPrice(),
                livroRequest.getAutorId(),
                livroRequest.getCategoryId()
        );

        // Verificar erro 422
        ValidateError validateError = spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(livroRequest)
                .when()
                .post(LivroCreateController.LIVRO_CREATED)
                .then()
                .statusCode(422)
                .extract()
                .as(ValidateError.class);

        assertValidationError(validateError, "O resumo é obrigatório");
    }

    @Test
    void testgetResumeMaxLengthValidation(RequestSpecification spec) {
        // Obter token
        String token = new AutenticationUtils(spec).getToken();

        // Criar categoria e autor
        UUID getCategoryId = createCategory(spec, token);
        UUID getAutorId = createAutor(spec, token);

        // Criar resumo com mais de 500 caracteres
        String longgetResume = "a".repeat(501);

        // Criar livro com resumo muito longo
        LivroCreatedRequest livroRequest = createValidLivroRequest(getAutorId, getCategoryId);
        livroRequest = new LivroCreatedRequest(
                livroRequest.getTitle(),
                longgetResume, // resumo longo demais
                livroRequest.getSummary(),
                livroRequest.getIsbn(),
                livroRequest.getNumberPages(),
                livroRequest.getPublicationDate(),
                livroRequest.getPrice(),
                livroRequest.getAutorId(),
                livroRequest.getCategoryId()
        );

        // Verificar erro 422
        ValidateError validateError = spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(livroRequest)
                .when()
                .post(LivroCreateController.LIVRO_CREATED)
                .then()
                .statusCode(422)
                .extract()
                .as(ValidateError.class);

        assertValidationError(validateError, "O resumo deve ter no máximo 400 caracteres");
    }

    @Test
    void testEmptygetIsbnValidation(RequestSpecification spec) {
        // Obter token
        String token = new AutenticationUtils(spec).getToken();

        // Criar categoria e autor
        UUID getCategoryId = createCategory(spec, token);
        UUID getAutorId = createAutor(spec, token);

        // Criar livro com getIsbn vazio
        LivroCreatedRequest livroRequest = createValidLivroRequest(getAutorId, getCategoryId);
        livroRequest = new LivroCreatedRequest(
                livroRequest.getTitle(),
                livroRequest.getResume(),
                livroRequest.getSummary(),
                "", // getIsbn vazio
                livroRequest.getNumberPages(),
                livroRequest.getPublicationDate(),
                livroRequest.getPrice(),
                livroRequest.getAutorId(),
                livroRequest.getCategoryId()
        );

        // Verificar erro 422
        ValidateError validateError = spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(livroRequest)
                .when()
                .post(LivroCreateController.LIVRO_CREATED)
                .then()
                .statusCode(422)
                .extract()
                .as(ValidateError.class);

        assertValidationError(validateError, "O ISBN é obrigatório");
    }

    @Test
    void testUniquegetIsbnValidation(RequestSpecification spec) {
        // Obter token
        String token = new AutenticationUtils(spec).getToken();

        // Criar categoria e autor
        UUID getCategoryId = createCategory(spec, token);
        UUID getAutorId = createAutor(spec, token);

        // Criar primeiro livro válido
        LivroCreatedRequest livroRequest = createValidLivroRequest(getAutorId, getCategoryId);

        // Primeira requisição (deve ser bem-sucedida)
        Response post = spec.given().contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(livroRequest)
                .when()
                .post(LivroCreateController.LIVRO_CREATED);

        JsonPath jsonPath = post.jsonPath();

        // Verificar status 201
        post.then()
                .statusCode(HttpStatus.CREATED.getCode());
        // Criar segundo livro com título diferente mas mesmo getIsbn
        LivroCreatedRequest secondLivroRequest = new LivroCreatedRequest(
                "Título Diferente " + System.currentTimeMillis(),
                livroRequest.getResume(),
                livroRequest.getSummary(),
                livroRequest.getIsbn(), // mesmo getIsbn
                livroRequest.getNumberPages(),
                livroRequest.getPublicationDate(),
                livroRequest.getPrice(),
                livroRequest.getAutorId(),
                livroRequest.getCategoryId()
        );

        // Segunda requisição com mesmo getIsbn (deve falhar)
        ValidateError validateError = spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(secondLivroRequest)
                .when()
                .post(LivroCreateController.LIVRO_CREATED)
                .then()
                .statusCode(422)
                .extract()
                .as(ValidateError.class);

        assertValidationError(validateError, "isbn já está em uso");
    }

    @Test
    void testgetNumberPagesMinValidation(RequestSpecification spec) {
        // Obter token
        String token = new AutenticationUtils(spec).getToken();

        // Criar categoria e autor
        UUID getCategoryId = createCategory(spec, token);
        UUID getAutorId = createAutor(spec, token);

        // Criar livro com número de páginas menor que o mínimo
        LivroCreatedRequest livroRequest = createValidLivroRequest(getAutorId, getCategoryId);
        livroRequest = new LivroCreatedRequest(
                livroRequest.getTitle(),
                livroRequest.getResume(),
                livroRequest.getSummary(),
                livroRequest.getIsbn(),
                99, // menos de 100 páginas
                livroRequest.getPublicationDate(),
                livroRequest.getPrice(),
                livroRequest.getAutorId(),
                livroRequest.getCategoryId()
        );

        // Verificar erro 422
        ValidateError validateError = spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(livroRequest)
                .when()
                .post(LivroCreateController.LIVRO_CREATED)
                .then()
                .statusCode(422)
                .extract()
                .as(ValidateError.class);

        assertValidationError(validateError, "O número mínimo de páginas é 100");
    }

    @Test
    void testgetNumberPagesNullValidation(RequestSpecification spec) {
        // Obter token
        String token = new AutenticationUtils(spec).getToken();

        // Criar categoria e autor
        UUID getCategoryId = createCategory(spec, token);
        UUID getAutorId = createAutor(spec, token);

        // Criar livro com número de páginas nulo
        LivroCreatedRequest livroRequest = createValidLivroRequest(getAutorId, getCategoryId);
        livroRequest = new LivroCreatedRequest(
                livroRequest.getTitle(),
                livroRequest.getResume(),
                livroRequest.getSummary(),
                livroRequest.getIsbn(),
                null, // páginas nulas
                livroRequest.getPublicationDate(),
                livroRequest.getPrice(),
                livroRequest.getAutorId(),
                livroRequest.getCategoryId()
        );

        // Verificar erro 422
        ValidateError validateError = spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(livroRequest)
                .when()
                .post(LivroCreateController.LIVRO_CREATED)
                .then()
                .statusCode(422)
                .extract()
                .as(ValidateError.class);

        assertValidationError(validateError, "O número de páginas é obrigatório");
    }

    @Test
    void testgetPublicationDateFutureValidation(RequestSpecification spec) {
        // Obter token
        String token = new AutenticationUtils(spec).getToken();

        // Criar categoria e autor
        UUID getCategoryId = createCategory(spec, token);
        UUID getAutorId = createAutor(spec, token);

        // Criar livro com data de publicação no passado
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);

        LivroCreatedRequest livroRequest = createValidLivroRequest(getAutorId, getCategoryId);
        livroRequest = new LivroCreatedRequest(
                livroRequest.getTitle(),
                livroRequest.getResume(),
                livroRequest.getSummary(),
                livroRequest.getIsbn(),
                livroRequest.getNumberPages(),
                pastDate, // data no passado
                livroRequest.getPrice(),
                livroRequest.getAutorId(),
                livroRequest.getCategoryId()
        );

        // Verificar erro 422
        ValidateError validateError = spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(livroRequest)
                .when()
                .post(LivroCreateController.LIVRO_CREATED)
                .then()
                .statusCode(422)
                .extract()
                .as(ValidateError.class);

        assertValidationError(validateError, "A data de publicação deve ser no futuro");
    }

    @Test
    void testgetPublicationDateNullValidation(RequestSpecification spec) {
        // Obter token
        String token = new AutenticationUtils(spec).getToken();

        // Criar categoria e autor
        UUID getCategoryId = createCategory(spec, token);
        UUID getAutorId = createAutor(spec, token);

        // Criar livro com data de publicação nula
        LivroCreatedRequest livroRequest = createValidLivroRequest(getAutorId, getCategoryId);
        livroRequest = new LivroCreatedRequest(
                livroRequest.getTitle(),
                livroRequest.getResume(),
                livroRequest.getSummary(),
                livroRequest.getIsbn(),
                livroRequest.getNumberPages(),
                null, // data nula
                livroRequest.getPrice(),
                livroRequest.getAutorId(),
                livroRequest.getCategoryId()
        );

        // Verificar erro 422
        ValidateError validateError = spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(livroRequest)
                .when()
                .post(LivroCreateController.LIVRO_CREATED)
                .then()
                .statusCode(422)
                .extract()
                .as(ValidateError.class);

        assertValidationError(validateError, "A data de publicação é obrigatória");
    }

    @Test
    void testgetPriceMinValidation(RequestSpecification spec) {
        // Obter token
        String token = new AutenticationUtils(spec).getToken();

        // Criar categoria e autor
        UUID getCategoryId = createCategory(spec, token);
        UUID getAutorId = createAutor(spec, token);

        // Criar livro com preço menor que o mínimo
        LivroCreatedRequest livroRequest = createValidLivroRequest(getAutorId, getCategoryId);
        livroRequest = new LivroCreatedRequest(
                livroRequest.getTitle(),
                livroRequest.getResume(),
                livroRequest.getSummary(),
                livroRequest.getIsbn(),
                livroRequest.getNumberPages(),
                livroRequest.getPublicationDate(),
                new BigDecimal("19.99"), // menos de 20
                livroRequest.getAutorId(),
                livroRequest.getCategoryId()
        );

        // Verificar erro 422
        ValidateError validateError = spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(livroRequest)
                .when()
                .post(LivroCreateController.LIVRO_CREATED)
                .then()
                .statusCode(422)
                .extract()
                .as(ValidateError.class);

        assertValidationError(validateError, "O preço mínimo é 20");
    }

    @Test
    void testgetPriceNullValidation(RequestSpecification spec) {
        // Obter token
        String token = new AutenticationUtils(spec).getToken();

        // Criar categoria e autor
        UUID getCategoryId = createCategory(spec, token);
        UUID getAutorId = createAutor(spec, token);

        // Criar livro com preço nulo
        LivroCreatedRequest livroRequest = createValidLivroRequest(getAutorId, getCategoryId);
        livroRequest = new LivroCreatedRequest(
                livroRequest.getTitle(),
                livroRequest.getResume(),
                livroRequest.getSummary(),
                livroRequest.getIsbn(),
                livroRequest.getNumberPages(),
                livroRequest.getPublicationDate(),
                null, // preço nulo
                livroRequest.getAutorId(),
                livroRequest.getCategoryId()
        );

        // Verificar erro 422
        ValidateError validateError = spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(livroRequest)
                .when()
                .post(LivroCreateController.LIVRO_CREATED)
                .then()
                .statusCode(422)
                .extract()
                .as(ValidateError.class);

        assertValidationError(validateError, "O preço é obrigatório");
    }

    @Test
    void testgetAutorIdNullValidation(RequestSpecification spec) {
        // Obter token
        String token = new AutenticationUtils(spec).getToken();

        // Criar categoria
        UUID getCategoryId = createCategory(spec, token);

        // Criar livro com getAutorId nulo
        LivroCreatedRequest livroRequest = createValidLivroRequest(UUID.randomUUID(), getCategoryId);
        livroRequest = new LivroCreatedRequest(
                livroRequest.getTitle(),
                livroRequest.getResume(),
                livroRequest.getSummary(),
                livroRequest.getIsbn(),
                livroRequest.getNumberPages(),
                livroRequest.getPublicationDate(),
                livroRequest.getPrice(),
                null, // getAutorId nulo
                livroRequest.getCategoryId()
        );

        // Verificar erro 422
        ValidateError validateError = spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(livroRequest)
                .when()
                .post(LivroCreateController.LIVRO_CREATED)
                .then()
                .statusCode(422)
                .extract()
                .as(ValidateError.class);

        assertValidationError(validateError, "O autor é obrigatório");
    }

    @Test
    void testgetAutorIdInexistenteValidation(RequestSpecification spec) {
        // Obter token
        String token = new AutenticationUtils(spec).getToken();

        // Criar categoria
        UUID getCategoryId = createCategory(spec, token);

        // Criar livro com getAutorId inexistente
        UUID inexistentegetAutorId = UUID.randomUUID();
        LivroCreatedRequest livroRequest = createValidLivroRequest(inexistentegetAutorId, getCategoryId);

        // Verificar erro 400
        spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(livroRequest)
                .when()
                .post(LivroCreateController.LIVRO_CREATED)
                .then()
                .statusCode(400); // Erro de serviço (O autor não existe)
    }

    @Test
    void testgetCategoryIdNullValidation(RequestSpecification spec) {
        // Obter token
        String token = new AutenticationUtils(spec).getToken();

        // Criar autor
        UUID getAutorId = createAutor(spec, token);

        // Criar livro com getCategoryId nulo
        LivroCreatedRequest livroRequest = createValidLivroRequest(getAutorId, UUID.randomUUID());
        livroRequest = new LivroCreatedRequest(
                livroRequest.getTitle(),
                livroRequest.getResume(),
                livroRequest.getSummary(),
                livroRequest.getIsbn(),
                livroRequest.getNumberPages(),
                livroRequest.getPublicationDate(),
                livroRequest.getPrice(),
                livroRequest.getAutorId(),
                null // getCategoryId nulo
        );

        // Verificar erro 422
        ValidateError validateError = spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(livroRequest)
                .when()
                .post(LivroCreateController.LIVRO_CREATED)
                .then()
                .statusCode(422)
                .extract()
                .as(ValidateError.class);

        assertValidationError(validateError, "A categoria é obrigatória");
    }

    @Test
    void testgetCategoryIdInexistenteValidation(RequestSpecification spec) {
        // Obter token
        String token = new AutenticationUtils(spec).getToken();

        // Criar autor
        UUID getAutorId = createAutor(spec, token);

        // Criar livro com getCategoryId inexistente
        UUID inexistentegetCategoryId = UUID.randomUUID();
        LivroCreatedRequest livroRequest = createValidLivroRequest(getAutorId, inexistentegetCategoryId);

        // Verificar erro 400
        spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(livroRequest)
                .when()
                .post(LivroCreateController.LIVRO_CREATED)
                .then()
                .statusCode(400); // Erro de serviço (A categoria não existe)
    }

    // Métodos auxiliares

    private UUID createCategory(RequestSpecification spec, String token) {

        if(categoriaGlobal != null){
            return this.categoriaGlobal;
        }

        String categoryName = "Categoria" + System.currentTimeMillis();
        CategoriaCreatedRequest categoryRequest = new CategoriaCreatedRequest(categoryName);

        // Criar categoria
        String locationHeader = spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(categoryRequest)
                .when()
                .post(CategoriaController.CATEGORIA_CREATED)
                .then()
                .statusCode(201)
                .extract()
                .header("Location");

        // Extrair ID da categoria da URL de localização
        String[] parts = locationHeader.split("/");
        this.categoriaGlobal =  UUID.fromString(parts[parts.length - 1]);

        return this.categoriaGlobal;
    }

    private UUID createAutor(RequestSpecification spec, String token) {

        if(this.autorGlobal != null){
            return this.autorGlobal;
        }

        // Aqui você precisará adaptar para o seu sistema - esse é apenas um exemplo
        String autorName = "Autor" + System.currentTimeMillis();
        String autorEmail = "autor" + System.currentTimeMillis() + "@example.com";

        // Assumindo que há um endpoint para criar autor
        // Se o seu sistema for diferente, ajuste conforme necessário
        AutorCreatedRequest autorRequest = new AutorCreatedRequest(autorName, autorEmail, "Descrição do autor");

        String locationHeader = spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(autorRequest)
                .when()
                .post("/api/v1/autores") // Ajuste para o endpoint correto
                .then()
                .statusCode(201)
                .extract()
                .header("Location");

        // Extrair ID do autor da URL de localização
        String[] parts = locationHeader.split("/");
        UUID uuidRequest = UUID.fromString(parts[parts.length - 1]);
        this.autorGlobal = uuidRequest;
        return uuidRequest;
    }

    private LivroCreatedRequest createValidLivroRequest(@NotNull UUID getAutorId, @NotNull UUID getCategoryId) {
        String uniqueId = String.valueOf(System.currentTimeMillis());

        // Garantir que os UUIDs sejam válidos
        String getAutorIdStr = getAutorId != null ? getAutorId.toString() : null;
        String getCategoryIdStr = getCategoryId != null ? getCategoryId.toString() : null;

        // Criar o objeto com todos os campos corretamente formatados
        return new LivroCreatedRequest(
                "Livro Teste " + uniqueId,         // getTitle
                "Resumo do livro teste",           // getResume
                "Sumário detalhado do livro",      // getSummary (pode ser null, certifique-se que seja válido)
                "getIsbn-" + uniqueId,                // getIsbn
                200,                               // getNumberPages
               LocalDateTime.now().plusMonths(1), // getPublicationDate
                new BigDecimal("49.90"),           // getPrice
                getAutorIdStr,                           // getAutorId
                getCategoryIdStr                       // getCategoryId
        );
    }

    private void assertValidationError(ValidateError validateError, String expectedMessage) {
        Assertions.assertNotNull(validateError, "Erro de validação não retornado");
        Assertions.assertNotNull(validateError.getErros(), "Lista de erros não retornada");
        Assertions.assertFalse(validateError.getErros().isEmpty(), "Lista de erros vazia");

        List<String> errors = validateError.getErros().stream()
                .map(FieldMessage::getMessage)
                .toList();

        Assertions.assertTrue(
                errors.stream().anyMatch(message -> message.contains(expectedMessage)),
                "Erro esperado não encontrado: " + expectedMessage + ". Erros retornados: " + errors
        );
    }
}
