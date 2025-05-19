package com.fsm.livraria.compra.entities;

import com.fsm.livraria.cupom.entities.Cupom;
import io.micronaut.data.annotation.Embeddable;
import io.micronaut.data.annotation.Relation;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

@Embeddable
public class CupomAplicado {

    @Relation(Relation.Kind.MANY_TO_ONE)
    private Cupom cupom;

    @Positive
    @NotNull
    private Double desconto;

    @Future
    @NotNull
    private LocalDateTime validade;


    @Deprecated
    public CupomAplicado() {
    }

    public CupomAplicado(Cupom cupom) {
        this.cupom = cupom;
        this.desconto = cupom.getDesconto();
        this.validade = cupom.getValidade();
    }

    public Cupom getCupom() {
        return cupom;
    }

    public void setCupom(Cupom cupom) {
        this.cupom = cupom;
    }

    public Double getDesconto() {
        return desconto;
    }

    public void setDesconto(Double desconto) {
        this.desconto = desconto;
    }

    public LocalDateTime getValidade() {
        return validade;
    }

    public void setValidade(LocalDateTime validade) {
        this.validade = validade;
    }
}
