package com.fsm.livraria.categoria.entities;

import com.fsm.base.model.BaseDomain;
import com.fsm.livraria.categoria.validation.CategoriaUniqueName;
import io.micronaut.data.annotation.MappedEntity;
import jakarta.validation.constraints.NotBlank;

@MappedEntity
public class Categoria extends BaseDomain {

    @NotBlank
    @CategoriaUniqueName
    String nome;

    /**
     * Esse construtor é necessário para o Micronaut Data
     * não deve ser utilizado diretamente
     * */
    @Deprecated
    public Categoria() {
    }

    public Categoria(@NotBlank @CategoriaUniqueName String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
