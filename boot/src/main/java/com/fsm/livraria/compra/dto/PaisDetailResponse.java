package com.fsm.livraria.compra.dto;

import com.fsm.livraria.pais.entities.Pais;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class PaisDetailResponse {

    String name;

    String uf;

    public PaisDetailResponse() {
    }

    public PaisDetailResponse(Pais pais) {
        name = pais.getName();
        uf = pais.getUf();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }
}
