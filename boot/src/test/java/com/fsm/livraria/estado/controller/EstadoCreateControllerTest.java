package com.fsm.livraria.estado.controller;

import com.fsm.exceptions.exception.FieldMessage;
import com.fsm.exceptions.exception.ValidateError;
import com.fsm.livraria.estado.dto.EstatoCreatedRequest;
import com.fsm.utils.AutenticationUtils;
import com.fsm.utils.PaisUtils;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class EstadoCreateControllerTest {

    @Test
    void EstadoCreateTest(RequestSpecification spec, PaisUtils paisUtils) {

        String token = new AutenticationUtils(spec).getToken();

        String name = "name" + System.currentTimeMillis();
        String uf = UUID.randomUUID().toString().substring(0, 2);

        UUID pais = paisUtils.getPais();


        EstatoCreatedRequest estadoRequest = new EstatoCreatedRequest(name, uf, pais.toString());

        spec.given()
                .contentType(ContentType.JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(estadoRequest)
            .when()
                .post(EstadoCreatedController.CREATE_ESTADOS)
            .then()
                .statusCode(201);
    }

    @Test
    void testErrorValidation(RequestSpecification spec) {
        String token = new AutenticationUtils(spec).getToken();

        // Campos inválidos
        String name = null;
        String uf = null;
        String paisId = null;

        EstatoCreatedRequest estadoRequest = new EstatoCreatedRequest(name, uf, paisId);
        ValidateError validateError = spec.given().contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(estadoRequest)
                .when()
                .post(EstadoCreatedController.CREATE_ESTADOS)
                .then()
                .statusCode(422)
                .extract()
                .as(ValidateError.class);

        assertNotNull(validateError, "Erro de validação não retornado");
        assertNotNull(validateError.getErros(), "Erro de validação não retornado");
        assertFalse(validateError.getErros().isEmpty(), "Erro de validação não retornado");

        List<String> errors = validateError.getErros().stream().map(FieldMessage::getMessage).toList();
        assertTrue(errors.contains("O nome do estado não pode ser vazio"), "Erro de validação não retornado");
        assertTrue(errors.contains("A UF do estado não pode ser vazia"), "Erro de validação não retornado");
        assertTrue(errors.contains("O ID do país não pode ser nulo"), "Erro de validação do país não retornado");
    }

    @Test
    void testErrorUfSizeValidation(RequestSpecification spec, PaisUtils paisUtils) {
        String token = new AutenticationUtils(spec).getToken();

        String name = "Estado " + System.currentTimeMillis();
        String uf = "ABC"; // UF com mais de 2 caracteres
        UUID paisId = paisUtils.getPais();

        EstatoCreatedRequest estadoRequest = new EstatoCreatedRequest(name, uf, paisId.toString());
        ValidateError validateError = spec.given().contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(estadoRequest)
                .when()
                .post(EstadoCreatedController.CREATE_ESTADOS)
                .then()
                .statusCode(422)
                .extract()
                .as(ValidateError.class);

        assertNotNull(validateError, "Erro de validação não retornado");
        assertNotNull(validateError.getErros(), "Erro de validação não retornado");
        assertFalse(validateError.getErros().isEmpty(), "Erro de validação não retornado");

        List<String> errors = validateError.getErros().stream().map(FieldMessage::getMessage).toList();
        assertTrue(errors.stream().anyMatch(msg -> msg.contains("A UF deve ter no máximo 2 caracteres")),
                "Erro de validação de tamanho da UF não retornado");
    }

    @Test
    void testErrorNameUniqueValidation(RequestSpecification spec, PaisUtils paisUtils) {
        String token = new AutenticationUtils(spec).getToken();

        String name = "Estado" + System.currentTimeMillis();
        String uf = UUID.randomUUID().toString().substring(0, 2);
        UUID paisId = paisUtils.getPais();

        EstatoCreatedRequest estadoRequest = new EstatoCreatedRequest(name, uf, paisId.toString());

        // Primeira requisição deve ter sucesso
        spec.given().contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(estadoRequest)
                .when()
                .post(EstadoCreatedController.CREATE_ESTADOS)
                .then()
                .statusCode(201);

        // Segunda requisição com o mesmo nome deve falhar
        ValidateError validateError = spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(estadoRequest)
                .when()
                .post(EstadoCreatedController.CREATE_ESTADOS)
                .then()
                .statusCode(422)
                .extract()
                .as(ValidateError.class);

        assertNotNull(validateError, "Erro de validação não retornado");
        assertNotNull(validateError.getErros(), "Erro de validação não retornado");
        assertFalse(validateError.getErros().isEmpty(), "Erro de validação não retornado");

        List<String> errors = validateError.getErros().stream().map(FieldMessage::getMessage).toList();
        assertTrue(errors.contains("O nome do estado já existe"), "Erro de validação não retornado");
    }

    @Test
    void testErrorPaisExistValidation(RequestSpecification spec) {
        String token = new AutenticationUtils(spec).getToken();

        String name = "Estado" + System.currentTimeMillis();
        String uf = UUID.randomUUID().toString().substring(0, 2);
        UUID paisInexistente = UUID.randomUUID(); // ID de país que não existe

        EstatoCreatedRequest estadoRequest = new EstatoCreatedRequest(name, uf, paisInexistente.toString());

        ValidateError validateError = spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(estadoRequest)
                .when()
                .post(EstadoCreatedController.CREATE_ESTADOS)
                .then()
                .statusCode(422)
                .extract()
                .as(ValidateError.class);

        assertNotNull(validateError, "Erro de validação não retornado");
        assertNotNull(validateError.getErros(), "Erro de validação não retornado");
        assertFalse(validateError.getErros().isEmpty(), "Erro de validação não retornado");

        List<String> errors = validateError.getErros().stream().map(FieldMessage::getMessage).toList();
        assertTrue(errors.contains("O pais não existe"), "Erro de validação não retornado");
    }
}
