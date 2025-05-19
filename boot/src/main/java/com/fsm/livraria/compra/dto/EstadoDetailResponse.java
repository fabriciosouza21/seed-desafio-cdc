package com.fsm.livraria.compra.dto;

import com.fsm.livraria.estado.entities.Estado;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class EstadoDetailResponse {

    private String name;

    private String uf;

    private PaisDetailResponse pais;

    public EstadoDetailResponse() {
    }

    public EstadoDetailResponse(Estado estado) {
        name = estado.getName();
        uf = estado.getUf();
        pais = new PaisDetailResponse(estado.getPais());
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

    public PaisDetailResponse getPais() {
        return pais;
    }

    public void setPais(PaisDetailResponse pais) {
        this.pais = pais;
    }
}
