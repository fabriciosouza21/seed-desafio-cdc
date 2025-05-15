package com.fsm.livraria.livro.controller;

import com.fsm.exceptions.exception.ServiceError;
import com.fsm.livraria.autor.dto.AutorDetail;
import com.fsm.livraria.livro.dto.LivroCreatedRequest;
import com.fsm.livraria.livro.dto.LivroDetail;
import com.fsm.livraria.livro.dto.LivroList;
import com.fsm.livraria.livro.repositories.LivroRepository;
import com.fsm.utils.AutenticationUtils;
import com.fsm.utils.AutorUtils;
import com.fsm.utils.CategoriaUtils;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
public class LivroDetailControllerTest {

    UUID livroId;

    @Test
    void testDetail(RequestSpecification spec,LivroRepository livroRepository , AutorUtils autorUtils, CategoriaUtils categoriaUtils) {
        UUID livro = getLivro(spec,livroRepository,autorUtils, categoriaUtils);
        String token = new AutenticationUtils(spec).getToken();

        LivroDetail livroDetail = spec.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .get(LivroDetailController.LIVRO_DETAIL, livro.toString())
                .then()
                .statusCode(200)
                .extract()
                .as(LivroDetail.class);
        assertNotNull(livroDetail);

        assertEquals(livro, livroDetail.getUuid());
        assertNotNull(livroDetail.getTitle(), "titulo");
        assertNotNull(livroDetail.getSummary(), "descricao");
        assertNotNull(livroDetail.getResume(), "resumo");
        assertNotNull(livroDetail.getIsbn(), "isbn");
        assertNotNull(livroDetail.getPrice(), "preco");
        assertNotNull(livroDetail.getPublicationDate(), "data de publicacao");
        AutorDetail author = livroDetail.getAuthor();
        assertNotNull(author, "autor");
        assertNotNull(livroDetail.getCategory(), "categoria");

        //autor
        assertNotNull(author.getName(), "nome do autor");
        assertNotNull(author.getDescricao(), "descricao do autor");

        //categoria
        assertNotNull(livroDetail.getCategory().getNome(), "nome da categoria");
    }

    @Test
    void testDetailNotFound(RequestSpecification spec) {
        String token = new AutenticationUtils(spec).getToken();

        String livroUuid = UUID.randomUUID().toString();
        Response response = spec.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .get(LivroDetailController.LIVRO_DETAIL, livroUuid)
                .then()
                .statusCode(404)
                .extract()
                .response();

        String errorMessage = response.jsonPath().getString("message");
        assertEquals("Livro não encontrado com o uuid: %s".formatted(livroUuid), errorMessage);
    }

    private UUID getLivro(RequestSpecification spec, LivroRepository livroRepository, AutorUtils autorUtils, CategoriaUtils categoriaUtils) {

        if (livroId != null) {
            return livroId;
        }

        long count = livroRepository.count();
        if(count == 0) {

            UUID autor = autorUtils.getAutor();

            UUID categoria = categoriaUtils.getCategoria();

            long ctm = System.currentTimeMillis();



            LivroCreatedRequest livroCreatedRequest = new LivroCreatedRequest(
                    "titulo" + ctm,
                    "descricao" + ctm,
                    "summary" + ctm,
                    "isbn" + ctm,
                    101,
                    LocalDateTime.now().plusMonths(1),
                    new BigDecimal("25.00"),
                    autor.toString(),
                    categoria.toString()
            );

            String token = new AutenticationUtils(spec).getToken();

            Response post = spec.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + token)
                    .body(livroCreatedRequest)
                    .when()
                    .post(LivroCreateController.LIVRO_CREATED);

            post
                    .then()
                    .statusCode(201);

            livroId = extrairId(post);


        } else {
            Page<LivroList> allLivros = livroRepository.findAllLivros(Pageable.from(0, 1));
            List<LivroList> livros = allLivros.getContent();

            if(livros.isEmpty()) {
                throw new ServiceError("deve haver pelo menos um livro");
            }
            LivroList first = livros.getFirst();
            livroId = first.getUuid();

            return livroId;
        }

        return livroId;

    }

    private UUID extrairId(Response response) {

        String locationHeader = response.then()
                .extract()
                .header("Location");
        // Extrair ID do autor da URL de localização
        String[] parts = locationHeader.split("/");

        return UUID.fromString(parts[parts.length - 1]);
    }


}
