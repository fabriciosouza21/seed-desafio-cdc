package com.fsm.livraria.cupom.entities;

import com.fsm.base.model.BaseDomain;
import com.fsm.exceptions.exception.ServiceError;
import io.micronaut.data.annotation.MappedEntity;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

@MappedEntity
public class Cupom extends BaseDomain {

    @NotBlank
    private String codigo;

    @Min(1)
    private Double desconto;

    @NotNull
    private LocalDateTime validade;

    /**
     * Não remover esse construtor padrão, ele é necessário para o Micronaut Data
     * e para a serialização/deserialização do JSON.
     * Não Utilizar esse construtor a não ser que haja um bom motivo.
     * */
    @Deprecated
    public Cupom() {
    }

    public Cupom(@NotBlank String codigo, @Min(1) Double desconto, @Future LocalDateTime validade) {

        if (codigo == null || codigo.isBlank()) {
            throw new ServiceError("O código é obrigatório");
        }

        if (desconto == null || desconto <= 0) {
            throw new ServiceError("O desconto deve ser maior que 0");
        }

        if (validade == null || !validade.isAfter(LocalDateTime.now())) {
            throw new ServiceError("A validade deve ser uma data futura");
        }

        this.codigo = codigo;
        this.desconto = desconto;
        this.validade = validade;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Cupom cupom = (Cupom) o;
        return Objects.equals(codigo, cupom.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), codigo);
    }

    public boolean hasValido() {
        return validade.isAfter(LocalDateTime.now());
    }
}
