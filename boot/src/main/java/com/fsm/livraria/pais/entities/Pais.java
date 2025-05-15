package com.fsm.livraria.pais.entities;

import com.fsm.base.model.BaseDomain;
import com.fsm.exceptions.exception.ServiceError;
import com.fsm.livraria.pais.validation.PaisUniqueName;
import io.micronaut.data.annotation.MappedEntity;
import jakarta.validation.constraints.NotBlank;

@MappedEntity
public class Pais extends BaseDomain {

    @NotBlank
    @PaisUniqueName
    String name;

    @NotBlank
    String uf;

    public Pais() {
    }

    public Pais(@PaisUniqueName @NotBlank String name, @NotBlank String uf) {


        if(name == null || name.isBlank()) {
            throw new ServiceError("Nome do país não pode ser vazio");
        }

        if(uf == null || uf.isBlank() || uf.length() > 2) {
            throw new ServiceError("UF não pode ser vazio");
        }

        this.name = name;

        this.uf = uf;
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
