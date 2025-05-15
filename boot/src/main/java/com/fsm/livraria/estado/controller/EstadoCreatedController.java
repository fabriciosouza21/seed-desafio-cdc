package com.fsm.livraria.estado.controller;

import com.fsm.livraria.estado.dto.EstatoCreatedRequest;
import com.fsm.livraria.estado.entities.Estado;
import com.fsm.livraria.estado.repositories.EstadoRepository;
import com.fsm.livraria.pais.repositories.PaisRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.security.annotation.Secured;
import jakarta.validation.Valid;

@Controller(value = EstadoCreatedController.CREATE_ESTADOS)
@Secured(value = "ROLE_ADMIN")
public class EstadoCreatedController {

    public static final String CREATE_ESTADOS = "/api/v1/estados";

    private final PaisRepository paisRepository;

    private final EstadoRepository estadoRepository;

    public EstadoCreatedController(PaisRepository paisRepository, EstadoRepository estadoRepository) {
        this.paisRepository = paisRepository;
        this.estadoRepository = estadoRepository;
    }

    @Post
    HttpResponse<Void> create(@Body @Valid EstatoCreatedRequest request) {
        Estado estado = request.toEntity(this.paisRepository);
        Estado estadoSaved = estadoRepository.save(estado);

        UriBuilder uri = UriBuilder.of(CREATE_ESTADOS).path(estadoSaved.getUuid().toString());

        return HttpResponse.created(uri.build());
    }
}
