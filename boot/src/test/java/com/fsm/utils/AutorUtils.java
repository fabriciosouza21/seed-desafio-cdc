package com.fsm.utils;

import com.fsm.exceptions.exception.NotFoundError;
import com.fsm.livraria.autor.dto.AutorCreatedRequest;
import com.fsm.livraria.autor.entities.Autor;
import com.fsm.livraria.autor.repositories.AutorRepository;
import io.micronaut.data.model.Pageable;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Singleton;
import org.apache.http.HttpHeaders;

import java.util.List;
import java.util.UUID;

@Singleton
public class AutorUtils {
    private UUID autorId;

    private final RequestSpecification spec;

    private final AutorRepository autorRepository;

    private final AutenticationUtils autenticationUtils;


    public AutorUtils(RequestSpecification spec, AutorRepository autorRepository, AutenticationUtils autenticationUtils) {
        this.spec = spec;
        this.autorRepository = autorRepository;
        this.autenticationUtils = autenticationUtils;
    }


    public UUID getAutor(){
        if (autorId != null) {
            return autorId;
        }

        long count = autorRepository.count();
        if(count == 0) {

            String token = autenticationUtils.getToken();
            String name = "testUser" + System.currentTimeMillis();
            String email = "testUser" + System.currentTimeMillis() + "@gmail.com";
            String description = "testUser" + System.currentTimeMillis();

            AutorCreatedRequest autorCreatedRequest = new AutorCreatedRequest(name, email, description);

            Response post = spec.given().contentType(ContentType.JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .body(autorCreatedRequest)
                    .when()
                    .post("/api/v1/autores");
            post
                    .then()
                    .statusCode(201);

            autorId = extrairIdAutor(post);

            return autorId;
        }else {
            Pageable pageable = Pageable.from(0, 1);
            List<Autor> autors = autorRepository.findAll(pageable);

            if(!autors.isEmpty()) {
                autorId = autors.getFirst().getUuid();
            }else {
                throw new NotFoundError("Autor não encontrado");
            }
            return autorId;
        }
    }

    private UUID extrairIdAutor(Response response) {

        String locationHeader = response.then()
                .extract()
                .header("Location");
        // Extrair ID do autor da URL de localização
        String[] parts = locationHeader.split("/");

        return UUID.fromString(parts[parts.length - 1]);
    }
}
