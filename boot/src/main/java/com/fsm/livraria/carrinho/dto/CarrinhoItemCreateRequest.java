package com.fsm.livraria.carrinho.dto;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Serdeable
public class CarrinhoItemCreateRequest {


    @NotBlank
    private String livroId;

    @Min(1)
    private Integer quantidade;

    public CarrinhoItemCreateRequest() {
    }

    public CarrinhoItemCreateRequest(String livroId, Integer quantidade) {
        this.livroId = livroId;
        this.quantidade = quantidade;
    }

    public String getLivroId() {
        return livroId;
    }

    public void setLivroId(String livroId) {
        this.livroId = livroId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }


}
