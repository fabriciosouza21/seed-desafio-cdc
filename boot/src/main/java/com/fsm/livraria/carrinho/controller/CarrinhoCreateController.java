package com.fsm.livraria.carrinho.controller;

import com.fsm.livraria.carrinho.dto.CarrinhoCreateRequest;
import com.fsm.livraria.carrinho.entities.Carrinho;
import com.fsm.livraria.carrinho.repositories.CarrinhoItemRepository;
import com.fsm.livraria.carrinho.repositories.CarrinhoRepository;
import com.fsm.livraria.livro.repositories.LivroRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.security.annotation.Secured;
import jakarta.validation.Valid;

import java.net.URI;

@Controller(value = CarrinhoCreateController.CARRINHO_CRIACAO)
@Secured("ROLE_ADMIN")
public class CarrinhoCreateController {

    public static final String CARRINHO_CRIACAO = "/api/v1/carrinhos";

    private final CarrinhoRepository carrinhoRepository;

    private final LivroRepository livroRepository;

    public CarrinhoCreateController(CarrinhoRepository carrinhoRepository, CarrinhoItemRepository carrinhoItemRepository, LivroRepository livroRepository) {
        this.carrinhoRepository = carrinhoRepository;
        this.livroRepository = livroRepository;
    }


    @Post
    HttpResponse<Void> create(@Body @Valid CarrinhoCreateRequest carrinhoCriacaoRequest) {

        //criar o carrinho
        Carrinho carrinhoNovo = carrinhoCriacaoRequest.toEntity(livroRepository);
        Carrinho carrinhoSaved = carrinhoRepository.save(carrinhoNovo);

        URI uri = UriBuilder.of(CARRINHO_CRIACAO).path(carrinhoSaved.getUuid().toString()).build();

        return HttpResponse.created(uri);
    }

}
