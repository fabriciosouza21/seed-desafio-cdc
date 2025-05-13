package com.fsm.livraria.controller;


import com.fsm.livraria.dto.AutorCreatedRequest;
import com.fsm.livraria.entities.Autor;
import com.fsm.livraria.repositories.AutorRepository;
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

@Controller(value = AutorCreateController.AUTOR_CREATED)
@Secured("ROLE_ADMIN")
public class AutorCreateController {

    public static final String AUTOR_CREATED = "/api/v1/autores";

    private final Logger Log = LoggerFactory.getLogger(AutorCreateController.class);

    private final AutorRepository autorRepository;

    public AutorCreateController(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    @Post
    public HttpResponse<Void> create(@Body @Valid AutorCreatedRequest request) {

        Log.info("Criando autor {}", request.name());

        Autor autor = request.toEntity();

        Autor autorSaved = autorRepository.save(autor);

        Log.info("Autor criado {}", autor);

        URI uri = UriBuilder.of(AUTOR_CREATED)
                .path(autorSaved.getUuid().toString())
                .build();

        return HttpResponse.created(uri);
    }


}
