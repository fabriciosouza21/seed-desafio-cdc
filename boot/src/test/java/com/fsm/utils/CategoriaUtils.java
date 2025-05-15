package com.fsm.utils;

import com.fsm.exceptions.exception.NotFoundError;
import com.fsm.livraria.autor.entities.Autor;
import com.fsm.livraria.categoria.controller.CategoriaController;
import com.fsm.livraria.categoria.dto.CategoriaCreatedRequest;
import com.fsm.livraria.categoria.entities.Categoria;
import com.fsm.livraria.categoria.repositories.CategoriaRepository;
import io.micronaut.data.model.Pageable;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Singleton;
import org.apache.http.HttpHeaders;

import java.util.List;
import java.util.UUID;

@Singleton
public class CategoriaUtils {
    private UUID categoriaId;

    private final RequestSpecification spec;

    private final CategoriaRepository categoriaRepository;

    private final AutenticationUtils autenticationUtils;


    public CategoriaUtils(RequestSpecification spec, CategoriaRepository categoriaRepository, AutenticationUtils autenticationUtils) {
        this.spec = spec;
        this.categoriaRepository = categoriaRepository;
        this.autenticationUtils = autenticationUtils;
    }

    public UUID getCategoria(){

        if (categoriaId != null) {
            return categoriaId;
        }

        long count = categoriaRepository.count();
        if(count == 0) {

            String token = autenticationUtils.getToken();
            String name = "testUser" + System.currentTimeMillis();

            CategoriaCreatedRequest categoriaCreatedRequest = new CategoriaCreatedRequest(name);

            Response post = spec.given().contentType(ContentType.JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .body(categoriaCreatedRequest)
                    .when()
                    .post(CategoriaController.CATEGORIA_CREATED);

            post
                    .then()
                    .statusCode(201);

            categoriaId = extrairId(post);

            return categoriaId;
        }else {
            Pageable pageable = Pageable.from(0, 1);
            List<Categoria> categoria = categoriaRepository.findAll(pageable);

            if(!categoria.isEmpty()) {
                categoriaId = categoria.getFirst().getUuid();
            }else {
                throw new NotFoundError("categoria não encontrado");
            }
            return categoriaId;
        }
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
