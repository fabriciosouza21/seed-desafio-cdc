package com.fsm.livraria.livro.controller;

import com.fsm.livraria.livro.dto.LivroList;
import com.fsm.utils.AutenticationUtils;
import io.micronaut.data.model.Page;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class LivroListControllerTest {

    @Test
    void testListLivros(RequestSpecification spec) {
        // Obtém token de autenticação
        String token = new AutenticationUtils(spec).getToken();

        // Faz requisição para listar livros - página padrão
        Page<?> response = spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .when()
                .get(LivroListController.LIVRO_LIST)
                .then()
                .statusCode(HttpStatus.OK.getCode())
                .extract()
                .as(Page.class);

        // Verifica se a resposta contém os campos esperados de uma Page
        assertNotNull(response);
        assertNotNull(response.getContent());
    }

    @Test
    void testListLivrosWithPagination(RequestSpecification spec) {
        // Obtém token de autenticação
        String token = new AutenticationUtils(spec).getToken();

        // Define parâmetros de paginação específicos
        Map<String, Object> queryParams = Map.of(
                "page", 0,
                "size", 5,
                "sort", "titulo,asc"
        );

        // Faz requisição com parâmetros de paginação
        Map<String, Object> response = spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .params(queryParams)
                .when()
                .get(LivroListController.LIVRO_LIST)
                .then()
                .statusCode(HttpStatus.OK.getCode())
                .extract()
                .as(Map.class);

        assertNotNull(response.get("content"));


        List<LivroList>  livros = (List<LivroList>)  response.get("content");

        assertEquals(17, response.get("totalSize"));

        assertEquals(5, livros.size());


    }

    @Test
    void testUnauthorizedAccess(RequestSpecification spec) {
        // Tenta acessar sem token de autenticação
        spec.given()
                .contentType(ContentType.JSON)
                .when()
                .get(LivroListController.LIVRO_LIST)
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.getCode());
    }

    @Test
    void testTypedResponse(RequestSpecification spec) {
        // Obtém token de autenticação
        String token = new AutenticationUtils(spec).getToken();

        // Faz a requisição e verifica se o conteúdo da página contém os campos esperados
        String response = spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .when()
                .get(LivroListController.LIVRO_LIST)
                .then()
                .statusCode(HttpStatus.OK.getCode())
                .extract()
                .asString();

        // Verifica presença de campos no JSON de resposta
        assertTrue(response.contains("content"));
        assertTrue(response.contains("totalSize"));

        // Verifica que o conteúdo inclui os campos title e uuid
        assertTrue(response.contains("title"));
        assertTrue(response.contains("uuid"));
    }
}
