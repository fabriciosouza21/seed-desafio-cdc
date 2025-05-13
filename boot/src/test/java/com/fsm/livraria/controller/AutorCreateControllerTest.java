package com.fsm.livraria.controller;

import com.fsm.livraria.dto.AutorCreatedRequest;
import com.fsm.utils.AutenticationUtils;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;

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
}
