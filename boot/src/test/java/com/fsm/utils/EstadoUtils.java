package com.fsm.utils;

import com.fsm.exceptions.exception.NotFoundError;
import com.fsm.livraria.estado.controller.EstadoCreatedController;
import com.fsm.livraria.estado.dto.EstatoCreatedRequest;
import com.fsm.livraria.estado.entities.Estado;
import com.fsm.livraria.estado.repositories.EstadoRepository;
import io.micronaut.data.model.Pageable;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Singleton;
import org.apache.http.HttpHeaders;

import java.util.List;
import java.util.UUID;

@Singleton
public class EstadoUtils {
    private Estado estado;

    private final PaisUtils paisUtils;

    private final RequestSpecification spec;

    private final EstadoRepository estadoRepository;

    private final AutenticationUtils autenticationUtils;



    public EstadoUtils(PaisUtils paisUtils, RequestSpecification spec, EstadoRepository estadoRepository, AutenticationUtils autenticationUtils) {
        this.paisUtils = paisUtils;
        this.spec = spec;
        this.estadoRepository = estadoRepository;
        this.autenticationUtils = autenticationUtils;
    }

    public Estado getEstado(){

        if (estado != null) {
            return estado;
        }

        long count = estadoRepository.count();
        if(count == 0) {

            String token = autenticationUtils.getToken();

            String name = "name" + System.currentTimeMillis();
            String uf = UUID.randomUUID().toString().substring(0, 2);

            UUID pais = paisUtils.getPais();


            EstatoCreatedRequest estadoRequest = new EstatoCreatedRequest(name, uf, pais.toString());

            Response post = spec.given()
                    .contentType(ContentType.JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .body(estadoRequest)
                    .when()
                    .post(EstadoCreatedController.CREATE_ESTADOS);


            post
                    .then()
                    .statusCode(201);

            UUID uuid = extrairId(post);
            estado = estadoRepository.findByUuid(uuid)
                    .orElseThrow(() -> new NotFoundError("Estado não encontrado"));

        }else {
            Pageable pageable = Pageable.from(0, 1);
            List<Estado> estados = estadoRepository.findAll(pageable);

            if(!estados.isEmpty()) {
                estado = estados.getFirst();
            }
        }
        return estado;
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
