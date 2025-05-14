package com.fsm.livraria.categoria.controller;

import com.fsm.livraria.categoria.dto.CategoriaCreatedRequest;
import com.fsm.livraria.categoria.entities.Categoria;
import com.fsm.livraria.categoria.repositories.CategoriaRepository;
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

@Controller(value = CategoriaController.CATEGORIA_CREATED)
@Secured("ROLE_ADMIN")
public class CategoriaController {
    public static final String CATEGORIA_CREATED = "/api/v1/categorias";

    private final Logger LOG = LoggerFactory.getLogger(CategoriaController.class);

    private final CategoriaRepository categoriaRepository;

    public CategoriaController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Post
    public HttpResponse<Void> create(@Body @Valid CategoriaCreatedRequest request) {

        LOG.info("Criando categoria {}", request.name());
        Categoria categoria = request.toEntity();
        Categoria categoriaSaved = categoriaRepository.save(categoria);
        URI uri = UriBuilder.of(CATEGORIA_CREATED).path(categoriaSaved.getUuid().toString()).build();
        LOG.info("Categoria criada {}", categoriaSaved);
        return HttpResponse.created(uri);
    }

}
