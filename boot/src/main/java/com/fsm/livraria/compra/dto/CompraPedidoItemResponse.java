package com.fsm.livraria.compra.dto;

import com.fsm.livraria.carrinho.entities.CarrinhoItem;
import com.fsm.livraria.livro.dto.LivroDetail;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class CompraPedidoItemResponse {

    private LivroDetail livroDetail;

    private Integer quantidade;

    public CompraPedidoItemResponse() {
    }

    public CompraPedidoItemResponse(CarrinhoItem item) {
        quantidade = item.getQuantidade();
        livroDetail = new LivroDetail(item.getLivro());
    }

    public LivroDetail getLivroDetail() {
        return livroDetail;
    }

    public void setLivroDetail(LivroDetail livroDetail) {
        this.livroDetail = livroDetail;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
