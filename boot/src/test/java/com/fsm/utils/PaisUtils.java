package com.fsm.utils;

import com.fsm.exceptions.exception.NotFoundError;
import com.fsm.livraria.pais.controller.PaisCreateController;
import com.fsm.livraria.pais.dto.PaisCreated;
import com.fsm.livraria.pais.entities.Pais;
import com.fsm.livraria.pais.repositories.PaisRepository;
import io.micronaut.data.model.Pageable;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Singleton;
import org.apache.http.HttpHeaders;

import java.util.List;
import java.util.UUID;

@Singleton
public class PaisUtils {
    private UUID paisId;

    private final RequestSpecification spec;

    private final PaisRepository paisRepository;

    private final AutenticationUtils autenticationUtils;


    public PaisUtils(RequestSpecification spec, PaisRepository paisRepository, AutenticationUtils autenticationUtils) {
        this.spec = spec;
        this.paisRepository = paisRepository;
        this.autenticationUtils = autenticationUtils;
    }

    public UUID getPais(){

        if (paisId != null) {
            return paisId;
        }

        long count = paisRepository.count();
        if(count == 0) {

            String name = "name" + System.currentTimeMillis();
            String uf = UUID.randomUUID().toString().substring(0, 2);

            PaisCreated paisCreated = new PaisCreated(name, uf);

            String token = autenticationUtils.getToken();

            Response post = spec.given().contentType(ContentType.JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .body(paisCreated)
                    .when()
                    .post(PaisCreateController.CREATE_PAISES);


            post
                    .then()
                    .statusCode(201);

            paisId = extrairId(post);

        }else {
            Pageable pageable = Pageable.from(0, 1);
            List<Pais> paises = paisRepository.findAll(pageable);

            if(!paises.isEmpty()) {
                paisId = paises.getFirst().getUuid();
            }else {
                throw new NotFoundError("categoria não encontrado");
            }
        }
        return paisId;
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
