package com.fsm.livraria.livro.controller;

import com.fsm.livraria.autor.repositories.AutorRepository;
import com.fsm.livraria.categoria.repositories.CategoriaRepository;
import com.fsm.livraria.livro.dto.LivroCreatedRequest;
import com.fsm.livraria.livro.entities.Livro;
import com.fsm.livraria.livro.repositories.LivroRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.security.annotation.Secured;
import jakarta.validation.Valid;

import java.net.URI;

@Controller(value = LivroCreateController.LIVRO_CREATED)
@Secured("ROLE_ADMIN")
public class LivroCreateController {

    public static final String LIVRO_CREATED = "/api/v1/livros";

    private final LivroRepository livroRepository;

    private final AutorRepository autorRepository;

    private final CategoriaRepository categoriaRepository;

    public LivroCreateController(LivroRepository livroRepository, AutorRepository autorRepository, CategoriaRepository categoriaRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Post
    HttpResponse<Void> create(@Body @Valid LivroCreatedRequest request) {

        Livro livro = request.toEntity(autorRepository, categoriaRepository);

        Livro livroSaved = livroRepository.save(livro);

        URI uri = UriBuilder.of(LIVRO_CREATED).path(livroSaved.getUuid().toString()).build();

        return HttpResponse.created(uri);
    }


}
