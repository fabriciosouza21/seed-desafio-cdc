package com.fsm.livraria.cupom.controller;

import com.fsm.exceptions.exception.FieldMessage;
import com.fsm.exceptions.exception.ValidateError;
import com.fsm.livraria.cupom.dto.CupomCreateRequest;
import com.fsm.utils.AutenticationUtils;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Inject;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class CupomControllerTest {

    @Inject
    AutenticationUtils autenticationUtils;

    @Test
    void testCreate(RequestSpecification spec) {
        String token = autenticationUtils.getToken();
        String codigo = "codigo" + System.currentTimeMillis();
        Double desconto = 10.0;
        LocalDateTime validade = LocalDateTime.now().plusDays(10);

        CupomCreateRequest cupomCreateRequest = new CupomCreateRequest(codigo, desconto, validade);

        spec.given().contentType(ContentType.JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(cupomCreateRequest)
                .when()
                .post(CupomController.CUPOM_CREATE)
                .then()
                .statusCode(201);
    }

    @Test
    void testErrorCodigoObrigatorio(RequestSpecification spec) {
        String token = autenticationUtils.getToken();

        // Código vazio
        String codigo = "";
        Double desconto = 10.0;
        LocalDateTime validade = LocalDateTime.now().plusDays(10);

        CupomCreateRequest cupomCreateRequest = new CupomCreateRequest(codigo, desconto, validade);

        ValidateError validateError = spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(cupomCreateRequest)
                .when()
                .post(CupomController.CUPOM_CREATE)
                .then()
                .statusCode(422)
                .extract()
                .as(ValidateError.class);

        assertNotNull(validateError, "Erro de validação não retornado");
        assertFalse(validateError.getErros().isEmpty(), "Erro de validação não retornado");

        List<String> errors = validateError.getErros().stream().map(FieldMessage::getMessage).toList();
        assertTrue(errors.contains("O código é obrigatório"), "Erro de validação não retornado");
    }

    @Test
    void testErrorCodigoUnico(RequestSpecification spec) {
        String token = autenticationUtils.getToken();

        // Primeiro cria um cupom válido
        String codigo = "codigo" + System.currentTimeMillis();
        Double desconto = 10.0;
        LocalDateTime validade = LocalDateTime.now().plusDays(10);

        CupomCreateRequest cupomCreateRequest = new CupomCreateRequest(codigo, desconto, validade);

        spec.given().contentType(ContentType.JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(cupomCreateRequest)
                .when()
                .post(CupomController.CUPOM_CREATE)
                .then()
                .statusCode(201);

        // Tentativa de criar cupom com mesmo código
        ValidateError validateError = spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(cupomCreateRequest)
                .when()
                .post(CupomController.CUPOM_CREATE)
                .then()
                .statusCode(422)
                .extract()
                .as(ValidateError.class);

        assertNotNull(validateError, "Erro de validação não retornado");
        assertNotNull(validateError.getErros(), "Erro de validação não retornado");
        assertFalse(validateError.getErros().isEmpty(), "Erro de validação não retornado");

        // Verifique a mensagem exata da sua validação @CupomUniqueCodigo
        // Esta mensagem pode variar dependendo da implementação
        List<String> errors = validateError.getErros().stream().map(FieldMessage::getMessage).toList();
        assertTrue(errors.stream().anyMatch(msg -> msg.contains("código")),
                "Erro de validação de código único não retornado");
    }

    @Test
    void testErrorDescontoObrigatorio(RequestSpecification spec) {
        String token = autenticationUtils.getToken();

        // Desconto nulo
        String codigo = "codigo" + System.currentTimeMillis();
        Double desconto = null;
        LocalDateTime validade = LocalDateTime.now().plusDays(10);

        CupomCreateRequest cupomCreateRequest = new CupomCreateRequest(codigo, desconto, validade);

        ValidateError validateError = spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(cupomCreateRequest)
                .when()
                .post(CupomController.CUPOM_CREATE)
                .then()
                .statusCode(422)
                .extract()
                .as(ValidateError.class);

        assertNotNull(validateError, "Erro de validação não retornado");
        assertFalse(validateError.getErros().isEmpty(), "Erro de validação não retornado");

        List<String> errors = validateError.getErros().stream().map(FieldMessage::getMessage).toList();
        assertTrue(errors.contains("O tipo de desconto é obrigatório"), "Erro de validação não retornado");
    }

    @Test
    void testErrorDescontoMinimo(RequestSpecification spec) {
        String token = autenticationUtils.getToken();

        // Desconto menor que 1
        String codigo = "codigo" + System.currentTimeMillis();
        Double desconto = 0.5;
        LocalDateTime validade = LocalDateTime.now().plusDays(10);

        CupomCreateRequest cupomCreateRequest = new CupomCreateRequest(codigo, desconto, validade);

        ValidateError validateError = spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(cupomCreateRequest)
                .when()
                .post(CupomController.CUPOM_CREATE)
                .then()
                .statusCode(422)
                .extract()
                .as(ValidateError.class);

        assertNotNull(validateError, "Erro de validação não retornado");
        assertFalse(validateError.getErros().isEmpty(), "Erro de validação não retornado");

        List<String> errors = validateError.getErros().stream().map(FieldMessage::getMessage).toList();
        assertTrue(errors.contains("O tipo de desconto deve ser maior que 0"), "Erro de validação não retornado");
    }

    @Test
    void testErrorValidadeObrigatoria(RequestSpecification spec) {
        String token = autenticationUtils.getToken();

        // Validade nula
        String codigo = "codigo" + System.currentTimeMillis();
        Double desconto = 10.0;
        LocalDateTime validade = null;

        CupomCreateRequest cupomCreateRequest = new CupomCreateRequest(codigo, desconto, validade);

        ValidateError validateError = spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(cupomCreateRequest)
                .when()
                .post(CupomController.CUPOM_CREATE)
                .then()
                .statusCode(422)
                .extract()
                .as(ValidateError.class);

        assertNotNull(validateError, "Erro de validação não retornado");
        assertFalse(validateError.getErros().isEmpty(), "Erro de validação não retornado");

        List<String> errors = validateError.getErros().stream().map(FieldMessage::getMessage).toList();
        assertTrue(errors.contains("A validade é obrigatória"), "Erro de validação não retornado");
    }

    @Test
    void testErrorValidadeFutura(RequestSpecification spec) {
        String token = autenticationUtils.getToken();

        // Validade no passado
        String codigo = "codigo" + System.currentTimeMillis();
        Double desconto = 10.0;
        LocalDateTime validade = LocalDateTime.now().minusDays(1);

        CupomCreateRequest cupomCreateRequest = new CupomCreateRequest(codigo, desconto, validade);

        ValidateError validateError = spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(cupomCreateRequest)
                .when()
                .post(CupomController.CUPOM_CREATE)
                .then()
                .statusCode(422)
                .extract()
                .as(ValidateError.class);

        assertNotNull(validateError, "Erro de validação não retornado");
        assertFalse(validateError.getErros().isEmpty(), "Erro de validação não retornado");

        List<String> errors = validateError.getErros().stream().map(FieldMessage::getMessage).toList();
        assertTrue(errors.contains("A validade deve ser uma data futura"), "Erro de validação não retornado");
    }
}
