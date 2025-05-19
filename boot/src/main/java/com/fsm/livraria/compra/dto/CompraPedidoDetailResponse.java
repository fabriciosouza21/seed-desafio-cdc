package com.fsm.livraria.compra.dto;

import com.fsm.livraria.carrinho.entities.Carrinho;
import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Serdeable
public class CompraPedidoDetailResponse {

    private BigDecimal total;


    List<CompraPedidoItemResponse> itens = new ArrayList<>();

    public CompraPedidoDetailResponse() {
    }

    public CompraPedidoDetailResponse(BigDecimal total, List<CompraPedidoItemResponse> itens) {
        this.total = total;
        this.itens = itens;
    }

    public CompraPedidoDetailResponse(Carrinho compraPedido) {
        total = compraPedido.getTotal();

        for (var item : compraPedido.getItens()) {
            itens.add(new CompraPedidoItemResponse(item));
        }
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<CompraPedidoItemResponse> getItens() {
        return itens;
    }

    public void setItens(List<CompraPedidoItemResponse> itens) {
        this.itens = itens;
    }
}
