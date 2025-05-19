package com.fsm.utils;

import com.fsm.exceptions.exception.NotFoundError;
import com.fsm.livraria.cupom.controller.CupomController;
import com.fsm.livraria.cupom.dto.CupomCreateRequest;
import com.fsm.livraria.cupom.entities.Cupom;
import com.fsm.livraria.cupom.repositories.CupomRepository;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Singleton;
import org.apache.http.HttpHeaders;

import java.time.LocalDateTime;
import java.util.UUID;

@Singleton
public class CupomUtils {
    private Cupom cupom;

    private final RequestSpecification spec;

    private final CupomRepository cupomRepository;

    private final AutenticationUtils autenticationUtils;


    public CupomUtils(RequestSpecification spec, CupomRepository cupomRepository, AutenticationUtils autenticationUtils) {
        this.spec = spec;
        this.cupomRepository = cupomRepository;
        this.autenticationUtils = autenticationUtils;
    }

    public Cupom getCupom(boolean cache) {
        if (cupom != null && cache) {
            return cupom;
        }

            String token = autenticationUtils.getToken();
            String codigo = "codigo-" + System.currentTimeMillis();
            Double desconto = 10.0;
            LocalDateTime validade = LocalDateTime.now().plusDays(10);

        CupomCreateRequest cupom = new CupomCreateRequest(codigo, desconto, validade);



        Response post = spec.given().contentType(ContentType.JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .body(cupom)
                    .when()
                    .post(CupomController.CUPOM_CREATE);
            post
                    .then()
                    .statusCode(201);

        Cupom cupomSaved = cupomRepository.findByUuid(extrairId(post)).orElseThrow(() -> new NotFoundError("Cupom não encontrado"));
        this.cupom = cupomSaved;

        return this.cupom;

    }

    public Cupom getCupom() {
        return getCupom(true);
    }

    private UUID extrairId(Response response) {

        String locationHeader = response.then()
                .extract()
                .header("Location");
        // Extrair ID do autor da URL de localização
        String[] parts = locationHeader.split("/");

        return UUID.fromString(parts[parts.length - 1]);
    }
}
