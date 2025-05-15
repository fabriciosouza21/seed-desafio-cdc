package com.fsm.livraria.categoria.dto;

import com.fsm.livraria.categoria.entities.Categoria;
import com.fsm.livraria.categoria.validation.CategoriaUniqueName;
import io.micronaut.data.annotation.AutoPopulated;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

@Serdeable
public class CategoriaDetail {

    private UUID uuid;

    String nome;

    /**
     * Esse construtor é necessário para o Micronaut Data
     * não deve ser utilizado diretamente
     * */
    @Deprecated
    public CategoriaDetail() {
    }


    public CategoriaDetail(@NotBlank UUID uuid,@NotBlank @CategoriaUniqueName String nome) {
        this.nome = nome;
    }

    public CategoriaDetail(Categoria categoria) {
        this.uuid = categoria.getUuid();
        this.nome = categoria.getNome();
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "nome='" + nome + '\'' +
                '}';
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
