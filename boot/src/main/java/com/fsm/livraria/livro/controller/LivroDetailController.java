package com.fsm.livraria.livro.controller;

import com.fsm.livraria.livro.dto.LivroDetail;
import com.fsm.livraria.livro.entities.Livro;
import com.fsm.livraria.livro.repositories.LivroRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.security.annotation.Secured;

@Controller(value = LivroDetailController.LIVRO_DETAIL)
@Secured("ROLE_ADMIN")
public class LivroDetailController {

    public  static final String LIVRO_DETAIL = "/api/v1/livros/{uuid}";

    private final LivroRepository livroRepository;


    public LivroDetailController(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    @Get
    public HttpResponse<LivroDetail> detail(@PathVariable String uuid) {
        Livro livro = livroRepository.findByUuidOrElseThrow(uuid);
        return HttpResponse.ok(new LivroDetail(livro));
    }
}
