package com.fsm.livraria.pais.controller;

import com.fsm.exceptions.exception.FieldMessage;
import com.fsm.exceptions.exception.ValidateError;
import com.fsm.livraria.pais.dto.PaisCreated;
import com.fsm.utils.AutenticationUtils;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Inject;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class PaisCreateControllerTest {

    @Inject
    private AutenticationUtils autenticationUtils;

    @Test
    void testCreatePais(RequestSpecification spec) {

        String token = autenticationUtils.getToken();

        String name = "name" + System.currentTimeMillis();
        String uf = UUID.randomUUID().toString().substring(0, 2);

        PaisCreated paisCreated = new PaisCreated(name, uf);

        spec.given().contentType(ContentType.JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(paisCreated)
                .when()
                .post(PaisCreateController.CREATE_PAISES)
                .then()
                .statusCode(201);
    }

    @Test
    void testErrorValidation(RequestSpecification spec) {

        String token = autenticationUtils.getToken();

        String name = null;
        String uf = null;

        PaisCreated paisCreated = new PaisCreated(name, uf);
        ValidateError validateError = spec.given().contentType(ContentType.JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(paisCreated)
                .when()
                .post(PaisCreateController.CREATE_PAISES)
                .then()
                .statusCode(422).extract().as(ValidateError.class);

        assertNotNull(validateError, "Erro de validação não retornado");

        assertNotNull(validateError.getErros(), "Erro de validação não retornado");

        assertFalse(validateError.getErros().isEmpty(), "Erro de validação não retornado");

        List<String> errors = validateError.getErros().stream().map(FieldMessage::getMessage).toList();
        assertTrue(errors.contains("Nome do país não pode ser vazio"), "Erro de validação não retornado");
        assertTrue(errors.contains("UF não pode ser vazio"), "Erro de validação não retornado");

    }

    @Test
    void testErrorNameUniqueValidation(RequestSpecification spec) {

        String token = autenticationUtils.getToken();

        String name = "name" + System.currentTimeMillis();
        String uf = UUID.randomUUID().toString().substring(0, 2);

        PaisCreated paisCreated = new PaisCreated(name, uf);

        spec.given().contentType(ContentType.JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(paisCreated)
                .when()
                .post(PaisCreateController.CREATE_PAISES)
                .then()
                .statusCode(201);



        // Repetindo o mesmo email para gerar erro de validação
        ValidateError validateError = spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(paisCreated)
                .when()
                .post(PaisCreateController.CREATE_PAISES)
                .then()
                .statusCode(422)
                .extract()
                .as(ValidateError.class);

        assertNotNull(validateError, "Erro de validação não retornado");

        assertNotNull(validateError.getErros(), "Erro de validação não retornado");
        assertFalse(validateError.getErros().isEmpty(), "Erro de validação não retornado");
        List<String> errors = validateError.getErros().stream().map(FieldMessage::getMessage).toList();
        assertTrue(errors.contains("O nome do país já existe"), "Erro de validação não retornado");

    }
}
