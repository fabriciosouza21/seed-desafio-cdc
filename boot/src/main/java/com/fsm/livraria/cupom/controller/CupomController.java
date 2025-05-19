package com.fsm.livraria.cupom.controller;

import com.fsm.livraria.cupom.dto.CupomCreateRequest;
import com.fsm.livraria.cupom.entities.Cupom;
import com.fsm.livraria.cupom.repositories.CupomRepository;
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


@Controller(CupomController.CUPOM_CREATE)
@Secured("ROLE_ADMIN")
public class CupomController {

    private static  final Logger LOG = LoggerFactory.getLogger(CupomController.class);

    public static final String CUPOM_CREATE = "/api/v1/cupom";

    private final CupomRepository cupomRepository;

    public CupomController(CupomRepository cupomRepository) {
        this.cupomRepository = cupomRepository;
    }

    @Post
    public HttpResponse<Void> create(@Body @Valid CupomCreateRequest cupomCreateRequest) {

        LOG.info("Criando cupom com o código: {}", cupomCreateRequest.codigo());
        Cupom cupom = cupomCreateRequest.toEntity();

        Cupom cupomSaved = cupomRepository.save(cupom);

        LOG.info("Cupom criado com sucesso: {}", cupomSaved.getCodigo());

        URI uri = UriBuilder.of(CUPOM_CREATE).path(cupomSaved.getUuid().toString()).build();

        return HttpResponse.created(uri);
    }


}
