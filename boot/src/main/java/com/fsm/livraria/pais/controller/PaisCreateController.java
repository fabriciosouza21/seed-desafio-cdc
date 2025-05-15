package com.fsm.livraria.pais.controller;

import com.fsm.livraria.pais.dto.PaisCreated;
import com.fsm.livraria.pais.entities.Pais;
import com.fsm.livraria.pais.repositories.PaisRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.security.annotation.Secured;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

@Controller(value = PaisCreateController.CREATE_PAISES)
@Secured(value = "ROLE_ADMIN")
public class PaisCreateController {

    public static final String CREATE_PAISES = "/api/v1/paises";

    private  static  final Logger LOG = LoggerFactory.getLogger(PaisCreateController.class);

    private final PaisRepository paisRepository;

    public PaisCreateController(PaisRepository paisRepository) {
        this.paisRepository = paisRepository;
    }

    @Post
    HttpResponse<Void> create(@Body @Valid PaisCreated paisCreated) {

        LOG.info("Criando país: {}", paisCreated.getName());

        Pais pais = paisCreated.toEntity();
        Pais paisSaved = paisRepository.save(pais);

        LOG.info("País criado com sucesso: {}", paisSaved.getUuid());

        URI uri = UriBuilder.of(CREATE_PAISES).path(paisSaved.getUuid().toString()).build();
        return HttpResponse.created(uri);
    }
}
