package com.fsm.livraria.compra.controller;

import com.fsm.livraria.carrinho.repositories.CarrinhoRepository;
import com.fsm.livraria.compra.dto.CompraCreateResquest;
import com.fsm.livraria.compra.entities.Compra;
import com.fsm.livraria.compra.entities.CompraPedido;
import com.fsm.livraria.compra.repositories.CompraPedidoRepository;
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

    private final CompraPedidoRepository compraPedidoRepository;

    public CompraCriacaoController(PaisRepository paisRepository, CupomRepository cupomRepository, CompraRepository compraRepository, EstadoRepository estadoRepository, CarrinhoRepository carrinhoRepository, CompraPedidoRepository compraPedidoRepository) {
        this.paisRepository = paisRepository;
        this.cupomRepository = cupomRepository;
        this.compraRepository = compraRepository;
        this.estadoRepository = estadoRepository;
        this.carrinhoRepository = carrinhoRepository;
        this.compraPedidoRepository = compraPedidoRepository;
    }

    @Post
    HttpResponse<Void> create(@Body @Valid CompraCreateResquest compraCriacaoRequest) {
        Compra compraNew = compraCriacaoRequest.toEntity(estadoRepository,paisRepository, cupomRepository);
        Compra compraSaved = compraRepository.save(compraNew);

        CompraPedido compraPedidoNew = compraCriacaoRequest.toCompraPedido(carrinhoRepository, compraSaved);
        compraPedidoRepository.save(compraPedidoNew);

        UriBuilder uri = UriBuilder.of(COMPRA_CRIACAO).path(compraSaved.getUuid().toString());
        return HttpResponse.created(uri.build());
    }
}
