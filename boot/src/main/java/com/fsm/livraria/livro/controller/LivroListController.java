package com.fsm.livraria.livro.controller;

import com.fsm.livraria.livro.dto.LivroList;
import com.fsm.livraria.livro.repositories.LivroRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import jakarta.validation.Valid;

@Controller(value = LivroListController.LIVRO_LIST)
@Secured("ROLE_ADMIN")
public class LivroListController {

    public static final String LIVRO_LIST = "/api/v1/livros";

    private final LivroRepository livroRepository;

    public LivroListController(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    @Get
    public HttpResponse<Page<LivroList>> list(@Valid Pageable pageable) {

        Page<LivroList> livroLists = livroRepository.findAllLivros(pageable);

        return HttpResponse.ok(livroLists);

    }

}
