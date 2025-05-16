package com.fsm.livraria.compra.controller;
import com.fsm.exceptions.exception.FieldMessage;
import com.fsm.exceptions.exception.ValidateError;
import com.fsm.livraria.compra.dto.CompraCreateResquest;
import com.fsm.livraria.estado.entities.Estado;
import com.fsm.utils.AutenticationUtils;
import com.fsm.utils.EstadoUtils;
import com.fsm.utils.PaisUtils;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Inject;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class CompraCriacaoControllerTest {

    @Inject
    private AutenticationUtils autenticationUtils;

    @Test
    void compraCriacaoTest(RequestSpecification spec, EstadoUtils estadoUtils) {
        String token = autenticationUtils.getToken();

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

        CompraCreateResquest compraRequest = criarCompraRequest(
                email, nome, sobrenome, documento, endereco, complemento,
                cidade, telefone, cep, estado.getUuid().toString(), estado.getPais().getUuid().toString()
        );

        // Testa a criação de compra com sucesso
        spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(compraRequest)
                .when()
                .post(CompraCriacaoController.COMPRA_CRIACAO)
                .then()
                .statusCode(201);
    }

    @Test
    void testErrorValidation(RequestSpecification spec) {
        String token = autenticationUtils.getToken();

        // Campos inválidos (nulos)
        CompraCreateResquest compraRequest = criarCompraRequest(
                null, null, null, null, null, null,
                null, null, null, null, null
        );

        ValidateError validateError = spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(compraRequest)
                .when()
                .post(CompraCriacaoController.COMPRA_CRIACAO)
                .then()
                .statusCode(422)
                .extract()
                .as(ValidateError.class);

        assertNotNull(validateError, "Erro de validação não retornado");
        assertNotNull(validateError.getErros(), "Erro de validação não retornado");
        assertFalse(validateError.getErros().isEmpty(), "Erro de validação não retornado");

        List<String> errors = validateError.getErros().stream().map(FieldMessage::getMessage).toList();

        // Verifica mensagens de erro para campos obrigatórios
        assertTrue(errors.contains("O email não pode ser vazio"), "Erro de validação do email não retornado");
        assertTrue(errors.contains("O nome não pode ser vazio"), "Erro de validação do nome não retornado");
        assertTrue(errors.contains("O sobrenome não pode ser vazio"), "Erro de validação do sobrenome não retornado");
        assertTrue(errors.contains("O documento não pode ser vazio"), "Erro de validação do documento não retornado");
        assertTrue(errors.contains("O endereço não pode ser vazio"), "Erro de validação do endereço não retornado");
        assertTrue(errors.contains("O complemento não pode ser vazio"), "Erro de validação do complemento não retornado");
        assertTrue(errors.contains("A cidade não pode ser vazia"), "Erro de validação da cidade não retornado");
        assertTrue(errors.contains("O telefone não pode ser vazio"), "Erro de validação do telefone não retornado");
        assertTrue(errors.contains("O cep não pode ser vazio"), "Erro de validação do cep não retornado");
        assertTrue(errors.contains("O estado não pode ser vazio"), "Erro de validação do estado não retornado");
        assertTrue(errors.contains("O país não pode ser vazio"), "Erro de validação do país não retornado");
    }

    @Test
    void testErrorEmailValidation(RequestSpecification spec, EstadoUtils estadoUtils) {
        String token = autenticationUtils.getToken();

        Estado estado = estadoUtils.getEstado();

        // Email com formato inválido
        String invalidEmail = "emailinvalido";

        CompraCreateResquest compraRequest = criarCompraRequest(
                invalidEmail, "Nome", "Sobrenome", "12345678909", "Endereço", "Complemento",
                "Cidade", "Telefone", "12345-678", estado.getUuid().toString(), estado.getPais().getUuid().toString()
        );

        ValidateError validateError = spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(compraRequest)
                .when()
                .post(CompraCriacaoController.COMPRA_CRIACAO)
                .then()
                .statusCode(422)
                .extract()
                .as(ValidateError.class);

        List<String> errors = validateError.getErros().stream().map(FieldMessage::getMessage).toList();
        assertTrue(errors.stream().anyMatch(msg -> msg.contains("O email deve ser válido")),
                "Erro de validação de email inválido não retornado");
    }

    @Test
    void testErrorDocumentoValidation(RequestSpecification spec, EstadoUtils estadoUtils) {
        String token = autenticationUtils.getToken();

        Estado estado = estadoUtils.getEstado();

        // Documento com formato inválido (nem CPF nem CNPJ válidos)
        String invalidDocumento = "123456";

        CompraCreateResquest compraRequest = criarCompraRequest(
                "email@teste.com", "Nome", "Sobrenome", invalidDocumento, "Endereço", "Complemento",
                "Cidade", "Telefone", "12345-678", estado.getUuid().toString(), estado.getPais().getUuid().toString()
        );

        ValidateError validateError = spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(compraRequest)
                .when()
                .post(CompraCriacaoController.COMPRA_CRIACAO)
                .then()
                .statusCode(422)
                .extract()
                .as(ValidateError.class);

        List<String> errors = validateError.getErros().stream().map(FieldMessage::getMessage).toList();
        assertTrue(errors.stream().anyMatch(msg -> msg.contains("Documento inválido")),
                "Documento inválido");
    }

    @Test
    void testErrorPaisExistValidation(RequestSpecification spec, EstadoUtils estadoUtils) {
        String token = autenticationUtils.getToken();

        Estado estado = estadoUtils.getEstado();
        UUID paisInexistente = UUID.randomUUID(); // ID de país que não existe

        CompraCreateResquest compraRequest = criarCompraRequest(
                "email@teste.com", "Nome", "Sobrenome", "12345678909", "Endereço", "Complemento",
                "Cidade", "Telefone", "12345-678", estado.getUuid().toString(), paisInexistente.toString()
        );

        ValidateError validateError = spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(compraRequest)
                .when()
                .post(CompraCriacaoController.COMPRA_CRIACAO)
                .then()
                .statusCode(422)
                .extract()
                .as(ValidateError.class);

        List<String> errors = validateError.getErros().stream().map(FieldMessage::getMessage).toList();
        assertTrue(errors.contains("O pais não existe"),
                "Erro de validação de país inexistente não retornado");
    }

    @Test
    void testErrorEstadoPaisRelacionamentoValidation(RequestSpecification spec, EstadoUtils estadoUtils, PaisUtils paisUtils) {
        String token = autenticationUtils.getToken();

        // Obtém um estado e um país diferente do país do estado
        Estado estado = estadoUtils.getEstado();


        UUID pais = paisUtils.getPaisExceto(estado.getPais().getUuid());

        CompraCreateResquest compraRequest = criarCompraRequest(
                "email@teste.com", "Nome", "Sobrenome", "12345678909", "Endereço", "Complemento",
                "Cidade", "Telefone", "12345-678", estado.getUuid().toString(), pais.toString()
        );

        ValidateError validateError = spec.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(compraRequest)
                .when()
                .post(CompraCriacaoController.COMPRA_CRIACAO)
                .then()
                .statusCode(422)
                .extract()
                .as(ValidateError.class);

        List<String> errors = validateError.getErros().stream().map(FieldMessage::getMessage).toList();
        assertTrue(errors.contains("O estado enviado não pertence ao país informado"),
                "O estado enviado não pertence ao país informado");
    }

    // Método auxiliar para criar uma instância de CompraCreateResquest
    private CompraCreateResquest criarCompraRequest(
            String email, String nome, String sobrenome, String documento,
            String endereco, String complemento, String cidade, String telefone,
            String cep, String estadoId, String paisId) {

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

        return request;
    }
}