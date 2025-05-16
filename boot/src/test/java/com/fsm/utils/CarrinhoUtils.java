package com.fsm.utils;

import com.fsm.livraria.carrinho.controller.CarrinhoCreateController;
import com.fsm.livraria.carrinho.dto.CarrinhoCreateRequest;
import com.fsm.livraria.carrinho.dto.CarrinhoItemCreateRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Singleton;
import org.apache.http.HttpHeaders;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Singleton
public class CarrinhoUtils {
    private UUID carrinhoId;

    private final RequestSpecification spec;

    private final AutenticationUtils autenticationUtils;


    private final LivroUtils livroUtils;


    public CarrinhoUtils(RequestSpecification spec, AutenticationUtils autenticationUtils, LivroUtils livroUtils) {
        this.spec = spec;
        this.autenticationUtils = autenticationUtils;
        this.livroUtils = livroUtils;
    }

    public UUID getCarrinho() {
        return getCarrinho(true);
    }


    public UUID getCarrinho(boolean cache){

        if (carrinhoId != null && cache) {
            return carrinhoId;
        }

        // Obter token
        String token = autenticationUtils.getToken();

        List<CarrinhoItemCreateRequest> items = List.of(createItem(livroUtils.getLivro(false)), createItem(livroUtils.getLivro(false)));

        CarrinhoCreateRequest carrinhoCreateRequest = new CarrinhoCreateRequest(new BigDecimal("100"), items);

        Response post = spec.given().contentType(ContentType.JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(carrinhoCreateRequest)
                .when()
                .post(CarrinhoCreateController.CARRINHO_CRIACAO);
        post
                .then()
                .statusCode(201);


            post
                    .then()
                    .statusCode(201);


        carrinhoId = extrairId(post);

        return carrinhoId;
    }

    private UUID extrairId(Response response) {

        String locationHeader = response.then()
                .extract()
                .header("Location");
        // Extrair ID do autor da URL de localização
        String[] parts = locationHeader.split("/");

        return UUID.fromString(parts[parts.length - 1]);
    }

    private CarrinhoItemCreateRequest createItem(UUID livroId) {
        return new CarrinhoItemCreateRequest(livroId.toString(), 1);
    }
}
