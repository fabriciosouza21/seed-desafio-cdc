package com.fsm.livraria.compra.controller;

import com.fsm.livraria.compra.dto.CompraCreateResquest;
import com.fsm.livraria.compra.dto.CompraDetailResponse;
import com.fsm.livraria.compra.dto.CompraPedidoDetailResponse;
import com.fsm.livraria.compra.dto.CompraPedidoItemResponse;
import com.fsm.livraria.compra.dto.EstadoDetailResponse;
import com.fsm.livraria.compra.dto.PaisDetailResponse;
import com.fsm.livraria.cupom.entities.Cupom;
import com.fsm.livraria.estado.entities.Estado;
import com.fsm.utils.AutenticationUtils;
import com.fsm.utils.CarrinhoUtils;
import com.fsm.utils.CupomUtils;
import com.fsm.utils.EstadoUtils;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Inject;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class CompraDetailControllerTest {

    UUID livroId;

    @Inject
    AutenticationUtils autenticationUtils;

    @Test
    void testDetail(RequestSpecification spec, EstadoUtils estadoUtils, CupomUtils cupomUtils, CarrinhoUtils carrinhoUtils) {
        // Obter token de autenticação
        String token = autenticationUtils.getToken();

        // Fazer requisição e obter resposta
        CompraDetailResponse response = spec.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .get(CompraDetailController.COMPRA_DETAIL, compraUUID(spec, estadoUtils, cupomUtils, carrinhoUtils))
                .then()
                .statusCode(200)
                .extract()
                .as(CompraDetailResponse.class);

        // Validar campos da resposta principal
        assertNotNull(response, "A resposta não deve ser nula");
        assertNotNull(response.getEmail(), "Email não deve ser nulo");
        assertNotNull(response.getNome(), "Nome não deve ser nulo");
        assertNotNull(response.getSobrenome(), "Sobrenome não deve ser nulo");
        assertNotNull(response.getDocumento(), "Documento não deve ser nulo");
        assertNotNull(response.getEndereco(), "Endereço não deve ser nulo");
        assertNotNull(response.getComplemento(), "Complemento não deve ser nulo");
        assertNotNull(response.getCidade(), "Cidade não deve ser nulo");
        assertNotNull(response.getTelefone(), "Telefone não deve ser nulo");
        assertNotNull(response.getCep(), "CEP não deve ser nulo");
        assertNotNull(response.getValorTotalDesconto(), "Valor total com desconto não deve ser nulo");

        // Validar EstadoDetailResponse
        EstadoDetailResponse estado = response.getEstado();
        assertNotNull(estado, "Objeto estado não deve ser nulo");
        assertNotNull(estado.getName(), "Nome do estado não deve ser nulo");
        assertNotNull(estado.getUf(), "UF do estado não deve ser nulo");

        // Validar PaisDetailResponse dentro de EstadoDetailResponse
        PaisDetailResponse pais = estado.getPais();
        assertNotNull(pais, "Objeto país não deve ser nulo");
        assertNotNull(pais.getName(), "Nome do país não deve ser nulo");
        assertNotNull(pais.getUf(), "UF do país não deve ser nulo");

        // Validar CompraPedidoDetailResponse
        CompraPedidoDetailResponse compraPedido = response.getCompraPedido();
        assertNotNull(compraPedido, "Objeto compraPedido não deve ser nulo");
        assertNotNull(compraPedido.getTotal(), "Total do pedido não deve ser nulo");
        assertNotNull(compraPedido.getItens(), "Lista de itens não deve ser nula");
        assertFalse(compraPedido.getItens().isEmpty(), "Lista de itens não deve estar vazia");

        // Validar primeiro item da lista (se existir)
        if (!compraPedido.getItens().isEmpty()) {
            CompraPedidoItemResponse primeiroItem = compraPedido.getItens().get(0);
            assertNotNull(primeiroItem, "Primeiro item não deve ser nulo");
            assertNotNull(primeiroItem.getQuantidade(), "Quantidade do item não deve ser nula");
            assertNotNull(primeiroItem.getLivroDetail(), "Detalhe do livro não deve ser nulo");

            // Aqui você poderia adicionar validações específicas para o LivroDetail,
            // mas como não tenho a classe, apenas verifico que o objeto existe
        }

        // Validações adicionais para assegurar consistência dos dados
        assertTrue(compraPedido.getTotal().compareTo(BigDecimal.ZERO) >= 0,
                "O total do pedido deve ser maior ou igual a zero");

        assertTrue(response.getValorTotalDesconto().compareTo(BigDecimal.ZERO) >= 0,
                "O valor total com desconto deve ser maior ou igual a zero");

        // Verificar se o valor com desconto é menor ou igual ao total
        assertTrue(response.getValorTotalDesconto().compareTo(compraPedido.getTotal()) <= 0,
                "Valor com desconto deve ser menor ou igual ao valor total");
    }

    private UUID compraUUID(RequestSpecification spec, EstadoUtils estadoUtils, CupomUtils cupomUtils, CarrinhoUtils carrinhoUtils) {
        String token = autenticationUtils.getToken();

        CompraCreateResquest compraRequest = getCompraCreateResquest(estadoUtils, cupomUtils, carrinhoUtils);

        // Testa a criação de compra com sucesso
        Response post = spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(compraRequest)
                .when()
                .post(CompraCriacaoController.COMPRA_CRIACAO);
        post
                .then()
                .statusCode(201);

        // Extrai o UUID da compra criada

        String locationHeader = post.then()
                .extract()
                .header("Location");
        return UUID.fromString(locationHeader.substring(locationHeader.lastIndexOf("/") + 1));
    }

    private CompraCreateResquest getCompraCreateResquest(EstadoUtils estadoUtils, CupomUtils cupomUtils, CarrinhoUtils carrinhoUtils) {
        // Obtém um estado existente com seu país associado
        Estado estado = estadoUtils.getEstado();

        // Dados da compra
        String email = "email" + System.currentTimeMillis() + "@teste.com";
        String nome = "Nome" + System.currentTimeMillis();
        String sobrenome = "Sobrenome" + System.currentTimeMillis();
        String documento = "12345678909"; // CPF válido
        String endereco = "Rua Teste, 123";
        String complemento = "Apto 101";
        String cidade = "Cidade Teste";
        String telefone = "(11) 98765-4321";
        String cep = "12345-678";

        Cupom cupom = cupomUtils.getCupom();

        CompraCreateResquest compraRequest = criarCompraRequest(
                email,
                nome,
                sobrenome,
                documento,
                endereco,
                complemento,
                cidade,
                telefone,
                cep,
                estado.getUuid().toString(),
                estado.getPais().getUuid().toString(),
                carrinhoUtils.getCarrinho().toString(),
                cupom.getCodigo()
        );
        return compraRequest;
    }

    private CompraCreateResquest criarCompraRequest(
            String email, String nome, String sobrenome, String documento,
            String endereco, String complemento, String cidade, String telefone,
            String cep, String estadoId, String paisId, String carrinhoId, String cupomCodigo) {

        CompraCreateResquest request = new CompraCreateResquest();
        request.setEmail(email);
        request.setNome(nome);
        request.setSobrenome(sobrenome);
        request.setDocumento(documento);
        request.setEndereco(endereco);
        request.setComplemento(complemento);
        request.setCidade(cidade);
        request.setTelefone(telefone);
        request.setCep(cep);
        request.setEstadoId(estadoId);
        request.setPaisId(paisId);
        request.setCarrinhoId(carrinhoId);
        request.setCupomCodigo(cupomCodigo);

        return request;
    }
}
