package com.fsm.livraria.compra.controller;

import com.fsm.livraria.compra.dto.CompraDetailResponse;
import com.fsm.livraria.compra.entities.Compra;
import com.fsm.livraria.compra.repositories.CompraRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.security.annotation.Secured;

import java.util.Optional;
import java.util.UUID;

@Controller(CompraDetailController.COMPRA_DETAIL)
@Secured("ROLE_ADMIN")
public class CompraDetailController {

    public static final String COMPRA_DETAIL = "/api/v1/compras/{uuid}";

    private final CompraRepository compraRepository;

    public CompraDetailController(CompraRepository compraRepository) {
        this.compraRepository = compraRepository;
    }


    @Get
    public HttpResponse<CompraDetailResponse> getCompraDetail(@PathVariable String uuid) {
        Optional<Compra> compra = compraRepository.findByUuid(UUID.fromString(uuid));
        return compra
                .map(c -> HttpResponse.ok(new CompraDetailResponse(c)))
                .orElseGet(HttpResponse::notFound);
    }

}
