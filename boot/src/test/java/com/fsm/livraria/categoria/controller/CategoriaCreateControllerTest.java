package com.fsm.livraria.categoria.controller;

import com.fsm.exceptions.exception.FieldMessage;
import com.fsm.exceptions.exception.ValidateError;
import com.fsm.livraria.categoria.dto.CategoriaCreatedRequest;
import com.fsm.utils.AutenticationUtils;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

@MicronautTest
public class CategoriaCreateControllerTest {

    @Test
    void testCreateAutor(RequestSpecification spec) {

        String token = new AutenticationUtils(spec).getToken();

        String name = "testUser" + System.currentTimeMillis();

        CategoriaCreatedRequest categoriaCreatedRequest = new CategoriaCreatedRequest(name);

        spec.given().contentType(ContentType.JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(categoriaCreatedRequest)
                .when()
                .post(CategoriaController.CATEGORIA_CREATED)
                .then()
                .statusCode(201);
    }

    @Test
    void testErrorValidation(RequestSpecification spec) {

        String token = new AutenticationUtils(spec).getToken();

        String name = null;

        CategoriaCreatedRequest categoriaCreatedRequest = new CategoriaCreatedRequest(name);

        spec.given().contentType(ContentType.JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(categoriaCreatedRequest)
                .when()
                .post(CategoriaController.CATEGORIA_CREATED)
                .then()
                .statusCode(422);
    }

    @Test
    void testErrorNameValidation(RequestSpecification spec) {

        String token = new AutenticationUtils(spec).getToken();

        String name = "testUser" + System.currentTimeMillis();

        CategoriaCreatedRequest categoriaCreatedRequest = new CategoriaCreatedRequest(name);

        spec.given().contentType(ContentType.JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(categoriaCreatedRequest)
                .when()
                .post(CategoriaController.CATEGORIA_CREATED)
                .then()
                .statusCode(201);


        // Repetindo o mesmo email para gerar erro de validação
        ValidateError validateError = spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(categoriaCreatedRequest)
                .when()
                .post(CategoriaController.CATEGORIA_CREATED)
                .then()
                .statusCode(422)
                .extract()
                .as(ValidateError.class);

        Assertions.assertNotNull(validateError, "Erro de validação não retornado");

        Assertions.assertNotNull(validateError.getErros(), "Erro de validação não retornado");
        Assertions.assertFalse(validateError.getErros().isEmpty(), "Erro de validação não retornado");
        List<String> errors = validateError.getErros().stream().map(FieldMessage::getMessage).toList();
        Assertions.assertTrue(errors.contains("nome já está em uso"), "Erro de validação não retornado");

    }
}
