package com.fsm.livraria.compra.entities;

import com.fsm.livraria.carrinho.entities.Carrinho;
import io.micronaut.data.annotation.Embeddable;
import io.micronaut.data.annotation.Relation;

import java.util.Objects;

@Embeddable
public class CompraPedidoId {

    @Relation(Relation.Kind.MANY_TO_ONE)
    private Compra compra;

    @Relation(Relation.Kind.MANY_TO_ONE)
    private Carrinho carrinho;


    public CompraPedidoId(Compra compra, Carrinho carrinho) {
        this.compra = compra;
        this.carrinho = carrinho;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public Carrinho getCarrinho() {
        return carrinho;
    }

    public void setCarrinho(Carrinho carrinho) {
        this.carrinho = carrinho;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CompraPedidoId that = (CompraPedidoId) o;
        return Objects.equals(compra, that.compra) && Objects.equals(carrinho, that.carrinho);
    }

    @Override
    public int hashCode() {
        return Objects.hash(compra, carrinho);
    }
}
