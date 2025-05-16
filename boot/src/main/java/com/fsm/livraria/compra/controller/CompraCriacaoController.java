package com.fsm.livraria.compra.controller;

import com.fsm.livraria.compra.dto.CompraCreateResquest;
import com.fsm.livraria.compra.entities.Compra;
import com.fsm.livraria.compra.repositories.CompraRepository;
import com.fsm.livraria.estado.repositories.EstadoRepository;
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

    private final CompraRepository compraRepository;

    private final EstadoRepository estadoRepository;

    public CompraCriacaoController(CompraRepository compraRepository, EstadoRepository estadoRepository) {
        this.compraRepository = compraRepository;
        this.estadoRepository = estadoRepository;
    }

    @Post
    HttpResponse<Void> create(@Body @Valid CompraCreateResquest compraCriacaoRequest) {
        Compra compraNew = compraCriacaoRequest.toEntity(estadoRepository);
        Compra compraSaved = compraRepository.save(compraNew);

        UriBuilder uri = UriBuilder.of(COMPRA_CRIACAO).path(compraSaved.getUuid().toString());
        return HttpResponse.created(uri.build());
    }
}
