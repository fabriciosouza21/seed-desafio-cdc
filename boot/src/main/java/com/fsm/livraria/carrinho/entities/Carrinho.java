package com.fsm.livraria.carrinho.entities;


import com.fsm.base.model.BaseDomain;
import com.fsm.exceptions.exception.ServiceError;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@MappedEntity
public class Carrinho extends BaseDomain {
    @Min(1)
    @NotNull
    private  BigDecimal total;

    @Size(min = 1)
    @NotNull
    @Relation(value = Relation.Kind.ONE_TO_MANY,mappedBy = "carrinho", cascade = Relation.Cascade.ALL)
    private List<CarrinhoItem> itens = new ArrayList<>();

    public Carrinho(BigDecimal total) {
        if(total == null || total.compareTo(BigDecimal.ZERO) < 0){
            throw new ServiceError("O total não pode ser nulo ou menor que zero");
        }
        this.total = total;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<CarrinhoItem> getItens() {
        return itens;
    }

    public void setItens(List<CarrinhoItem> itens) {
        this.itens = itens;
    }
}
