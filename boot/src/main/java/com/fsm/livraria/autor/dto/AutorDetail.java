package com.fsm.livraria.autor.dto;

import com.fsm.livraria.autor.entities.Autor;
import io.micronaut.serde.annotation.Serdeable;

import java.util.UUID;

@Serdeable
public class AutorDetail {

    private UUID uuid;


    private String name;


    private String descricao;

    @Deprecated
    public AutorDetail() {

    }

    public AutorDetail(Autor autor) {
        this.uuid = autor.getUuid();
        this.name = autor.getNome();
        this.descricao = autor.getDescricao();

    }


    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getName() {
        return name;
    }


    public String getDescricao() {
        return descricao;
    }
}
