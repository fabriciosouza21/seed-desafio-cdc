package com.fsm.livraria.compra.controller;

import com.fsm.livraria.carrinho.repositories.CarrinhoRepository;
import com.fsm.livraria.compra.dto.CompraCreateResquest;
import com.fsm.livraria.compra.entities.Compra;
import com.fsm.livraria.compra.repositories.CompraRepository;
import com.fsm.livraria.cupom.repositories.CupomRepository;
import com.fsm.livraria.estado.repositories.EstadoRepository;
import com.fsm.livraria.pais.repositories.PaisRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.security.annotation.Secured;
import jakarta.validation.Valid;

@Controller(value = CompraCriacaoController.COMPRA_CRIACAO)
@Secured("ROLE_ADMIN")
public class CompraCriacaoController {

    public static final String COMPRA_CRIACAO = "/api/v1/compras";

    private final PaisRepository paisRepository;

    private final CupomRepository cupomRepository;

    private final CompraRepository compraRepository;

    private final EstadoRepository estadoRepository;

    private final CarrinhoRepository carrinhoRepository;

    public CompraCriacaoController(PaisRepository paisRepository, CupomRepository cupomRepository, CompraRepository compraRepository, EstadoRepository estadoRepository, CarrinhoRepository carrinhoRepository) {
        this.paisRepository = paisRepository;
        this.cupomRepository = cupomRepository;
        this.compraRepository = compraRepository;
        this.estadoRepository = estadoRepository;
        this.carrinhoRepository = carrinhoRepository;
    }

    @Post
    HttpResponse<Void> create(@Body @Valid CompraCreateResquest compraCriacaoRequest) {
        Compra compraNew = compraCriacaoRequest.toEntity(estadoRepository,paisRepository, cupomRepository);
        Compra compraSaved = compraRepository.save(compraNew);
        compraCriacaoRequest.atribuirCarrinho(carrinhoRepository, compraSaved);
        compraRepository.update(compraSaved);
        UriBuilder uri = UriBuilder.of(COMPRA_CRIACAO).path(compraSaved.getUuid().toString());
        return HttpResponse.created(uri.build());
    }
}
