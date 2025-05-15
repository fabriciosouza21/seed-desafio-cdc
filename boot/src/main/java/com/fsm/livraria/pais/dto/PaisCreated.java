package com.fsm.livraria.pais.dto;

import com.fsm.livraria.pais.entities.Pais;
import com.fsm.livraria.pais.validation.PaisUniqueName;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Serdeable
public class PaisCreated {

    @NotBlank(message = "Nome do país não pode ser vazio")
    @PaisUniqueName
    private String name;

    @NotBlank(message = "UF não pode ser vazio")
    @Size(max = 2, message = "UF deve ter no máximo 2 caracteres")
    private String uf;

    public PaisCreated() {
    }

    public PaisCreated(String name, String uf) {
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

    public Pais toEntity() {
        return new Pais(name, uf);

    }
}
