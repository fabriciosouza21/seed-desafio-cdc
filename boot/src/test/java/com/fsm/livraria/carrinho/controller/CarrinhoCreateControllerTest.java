package com.fsm.livraria.carrinho.controller;

import com.fsm.exceptions.exception.FieldMessage;
import com.fsm.exceptions.exception.ValidateError;
import com.fsm.livraria.carrinho.dto.CarrinhoCreateRequest;
import com.fsm.livraria.carrinho.dto.CarrinhoItemCreateRequest;
import com.fsm.utils.AutenticationUtils;
import com.fsm.utils.LivroUtils;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Inject;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class CarrinhoCreateControllerTest {

    @Inject
    private AutenticationUtils autenticationUtils;

    @Inject
    private LivroUtils livroUtils;

    @Test
    void testCreate(RequestSpecification spec, AutenticationUtils autenticationUtils) {

        String token = autenticationUtils.getToken();

        List<CarrinhoItemCreateRequest> items = List.of(createItem(livroUtils.getLivro(false)), createItem(livroUtils.getLivro(false)));

        CarrinhoCreateRequest carrinhoCreateRequest = new CarrinhoCreateRequest(new BigDecimal("100"), items);

        spec.given().contentType(ContentType.JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(carrinhoCreateRequest)
                .when()
                .post(CarrinhoCreateController.CARRINHO_CRIACAO)
                .then()
                .statusCode(201);

    }

    @Test
    void testTotalNullValidation(RequestSpecification spec) {
        String token = autenticationUtils.getToken();

        List<CarrinhoItemCreateRequest> items = List.of(createItem(livroUtils.getLivro(false)));

        // Total nulo - deve falhar na validação @NotNull
        CarrinhoCreateRequest carrinhoCreateRequest = new CarrinhoCreateRequest(null, items);

        ValidateError validateError = spec.given().contentType(ContentType.JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(carrinhoCreateRequest)
                .when()
                .post(CarrinhoCreateController.CARRINHO_CRIACAO)
                .then()
                .statusCode(422)
                .extract()
                .as(ValidateError.class);

        assertNotNull(validateError, "Erro de validação não retornado");
        assertNotNull(validateError.getErros(), "Lista de erros não retornada");
        assertFalse(validateError.getErros().isEmpty(), "Lista de erros está vazia");
        List<String> errors = validateError.getErros().stream().map(FieldMessage::getMessage).toList();
        assertTrue(errors.contains("O total não pode ser nulo"), "Erro de validação de campo nulo não retornado");
    }

    @Test
    void testTotalMinValidation(RequestSpecification spec) {
        String token = autenticationUtils.getToken();

        List<CarrinhoItemCreateRequest> items = List.of(createItem(livroUtils.getLivro(false)));

        // Total com valor zero - deve falhar na validação @Min(1)
        CarrinhoCreateRequest carrinhoCreateRequest = new CarrinhoCreateRequest(BigDecimal.ZERO, items);

        ValidateError validateError = spec.given().contentType(ContentType.JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(carrinhoCreateRequest)
                .when()
                .post(CarrinhoCreateController.CARRINHO_CRIACAO)
                .then()
                .statusCode(422)
                .extract()
                .as(ValidateError.class);

        assertNotNull(validateError, "Erro de validação não retornado");
        assertFalse(validateError.getErros().isEmpty(), "Lista de erros está vazia");
        List<String> errors = validateError.getErros().stream().map(FieldMessage::getMessage).toList();
        assertTrue(errors.contains("O total não pode ser menor que 1"), "Erro de validação de valor mínimo não retornado");
    }

    @Test
    void testItensEmptyValidation(RequestSpecification spec) {
        String token = autenticationUtils.getToken();

        // Lista de itens vazia - deve falhar na validação @Size(min = 1)
        CarrinhoCreateRequest carrinhoCreateRequest = new CarrinhoCreateRequest(new BigDecimal("100"), Collections.emptyList());

        ValidateError validateError = spec.given().contentType(ContentType.JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(carrinhoCreateRequest)
                .when()
                .post(CarrinhoCreateController.CARRINHO_CRIACAO)
                .then()
                .statusCode(422)
                .extract()
                .as(ValidateError.class);

        assertNotNull(validateError, "Erro de validação não retornado");
        assertFalse(validateError.getErros().isEmpty(), "Lista de erros está vazia");
        List<String> errors = validateError.getErros().stream().map(FieldMessage::getMessage).toList();
        assertTrue(errors.contains("O carrinho deve ter pelo menos um item"), "Erro de validação de tamanho mínimo não retornado");
    }

    @Test
    void testLivroNaoEncontradoValidation(RequestSpecification spec) {
        String token = autenticationUtils.getToken();

        // Criando um item com ID de livro que não existe
        UUID livroExistente = livroUtils.getLivro(false);
        UUID livroInexistente = UUID.randomUUID(); // UUID aleatório que não existe no banco

        List<CarrinhoItemCreateRequest> items = List.of(
                createItem(livroExistente),
                createItem(livroInexistente)
        );

        CarrinhoCreateRequest carrinhoCreateRequest = new CarrinhoCreateRequest(new BigDecimal("100"), items);

        ValidateError validateError = spec.given().contentType(ContentType.JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(carrinhoCreateRequest)
                .when()
                .post(CarrinhoCreateController.CARRINHO_CRIACAO)
                .then()
                .statusCode(400) // Assumindo que o erro de serviço retorna 422
                .extract()
                .as(ValidateError.class);

        assertNotNull(validateError, "Erro de validação não retornado");
        assertTrue(validateError.getMessage().contains("livros não encontrados"), "Livro não encontrado não retornado");
    }

    @Test
    void testTotalIncorretoValidation(RequestSpecification spec) {
        String token = autenticationUtils.getToken();

        // Criando itens com valores conhecidos para testar a validação do total
        UUID livro1 = livroUtils.getLivro(false);
        UUID livro2 = livroUtils.getLivro(false);

        List<CarrinhoItemCreateRequest> items = List.of(
                createItem(livro1),
                createItem(livro2)
        );

        // Total incorreto (menor do que deveria ser de acordo com os preços dos livros)
        CarrinhoCreateRequest carrinhoCreateRequest = new CarrinhoCreateRequest(new BigDecimal("50"), items);

        ValidateError validateError = spec.given().contentType(ContentType.JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(carrinhoCreateRequest)
                .when()
                .post(CarrinhoCreateController.CARRINHO_CRIACAO)
                .then()
                .statusCode(400) // Assumindo que o erro de serviço retorna 422
                .extract()
                .as(ValidateError.class);

        assertNotNull(validateError, "Erro de validação não retornado");
        assertTrue(validateError.getMessage().contains("O total do carrinho não é igual ao total dos livros"), "Erro de validação de total incorreto não retornado");
    }


    private CarrinhoItemCreateRequest createItem(UUID livro) {
        return new CarrinhoItemCreateRequest(livro.toString(), 1);
    }
}
