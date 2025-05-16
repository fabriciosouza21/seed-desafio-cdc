package com.fsm.livraria.carrinho.entities;

import com.fsm.base.model.BaseDomain;
import com.fsm.livraria.livro.entities.Livro;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;

@MappedEntity
public class CarrinhoItem extends BaseDomain {

    @Relation(Relation.Kind.MANY_TO_ONE)
    private Livro livro;

    @Relation(Relation.Kind.MANY_TO_ONE)
    private Carrinho carrinho;

    private Integer quantidade;

    public CarrinhoItem(Livro livro, Carrinho carrinho ,Integer quantidade) {

        if(livro == null){
            throw new IllegalArgumentException("O livro não pode ser nulo");
        }

        if(carrinho == null){
            throw new IllegalArgumentException("O carrinho não pode ser nulo");
        }

        if(quantidade == null || quantidade < 1){
            throw new IllegalArgumentException("A quantidade não pode ser nula ou menor que 1");
        }

        this.livro = livro;
        this.quantidade = quantidade;
        this.carrinho = carrinho;
    }


    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Carrinho getCarrinho() {
        return carrinho;
    }

    public void setCarrinho(Carrinho carrinho) {
        this.carrinho = carrinho;
    }
}
