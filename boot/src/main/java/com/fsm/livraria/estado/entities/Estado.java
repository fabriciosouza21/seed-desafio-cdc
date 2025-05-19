package com.fsm.livraria.estado.entities;


import com.fsm.base.model.BaseDomain;
import com.fsm.exceptions.exception.ServiceError;
import com.fsm.livraria.pais.entities.Pais;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@MappedEntity
public class Estado extends BaseDomain {

    @NotBlank
    private String name;

    @NotBlank
    @Size(max = 2, message = "UF deve ter no máximo 2 caracteres")
    private String uf;

    @Relation(Relation.Kind.MANY_TO_ONE)
    private Pais pais;

    @Deprecated
    public Estado() {
    }

    public Estado(@NotBlank String name, @NotBlank @Size(max = 2) String uf, Pais pais) {

        if (name == null || name.isBlank()) {
            throw new ServiceError("Nome não pode ser nulo ou vazio");
        }

        if (uf == null || uf.isBlank() || uf.length() > 2) {
            throw new ServiceError("UF não pode ser nulo, vazio ou maior que 2 caracteres");
        }

        if (pais == null) {
            throw new ServiceError("País não pode ser nulo");
        }


        this.name = name;
        this.uf = uf;
        this.pais = pais;
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

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public boolean hasMeuPais(Pais pais) {
        if (pais == null) {
            return false;
        }

        return this.pais.getId().equals(pais.getId());
    }
}
