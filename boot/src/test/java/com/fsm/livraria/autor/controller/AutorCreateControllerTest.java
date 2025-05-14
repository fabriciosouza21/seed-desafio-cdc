package com.fsm.livraria.autor.controller;

import com.fsm.exceptions.exception.FieldMessage;
import com.fsm.exceptions.exception.ValidateError;
import com.fsm.livraria.autor.dto.AutorCreatedRequest;
import com.fsm.utils.AutenticationUtils;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

@MicronautTest
public class AutorCreateControllerTest {

    @Test
    void testCreateAutor(RequestSpecification spec) {

        String token = new AutenticationUtils(spec).getToken();

        String name = "testUser" + System.currentTimeMillis();
        String email = "testUser" + System.currentTimeMillis() + "@gmail.com";
        String description = "testUser" + System.currentTimeMillis();

        AutorCreatedRequest autorCreatedRequest = new AutorCreatedRequest(name, email, description);

        spec.given().contentType(ContentType.JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(autorCreatedRequest)
                .when()
                .post("/api/v1/autores")
                .then()
                .statusCode(201);
    }

    @Test
    void testErrorValidation(RequestSpecification spec) {

        String token = new AutenticationUtils(spec).getToken();

        String name = null;
        String email = null;
        String description = null;

        AutorCreatedRequest autorCreatedRequest = new AutorCreatedRequest(name, email, description);
        spec.given().contentType(ContentType.JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(autorCreatedRequest)
                .when()
                .post("/api/v1/autores")
                .then()
                .statusCode(422);
    }

    @Test
    void testErrorEmailValidation(RequestSpecification spec) {

        String token = new AutenticationUtils(spec).getToken();

        String name = "testUser" + System.currentTimeMillis();
        String email = "testUser" + System.currentTimeMillis() + "@gmail.com";
        String description = "testUser" + System.currentTimeMillis();

        AutorCreatedRequest autorCreatedRequest = new AutorCreatedRequest(name, email, description);

        spec.given().contentType(ContentType.JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(autorCreatedRequest)
                .when()
                .post("/api/v1/autores")
                .then()
                .statusCode(201);


        // Repetindo o mesmo email para gerar erro de validação
        ValidateError validateError = spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(autorCreatedRequest)
                .when()
                .post("/api/v1/autores")
                .then()
                .statusCode(422)
                .extract()
                .as(ValidateError.class);

        Assertions.assertNotNull(validateError, "Erro de validação não retornado");

        Assertions.assertNotNull(validateError.getErros(), "Erro de validação não retornado");
        Assertions.assertFalse(validateError.getErros().isEmpty(), "Erro de validação não retornado");
        List<String> errors = validateError.getErros().stream().map(FieldMessage::getMessage).toList();
        Assertions.assertTrue(errors.contains("E-mail já está em uso"), "Erro de validação não retornado");

    }
}
