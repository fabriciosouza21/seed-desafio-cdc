package com.fsm.livraria.estado.dto;

import com.fsm.exceptions.exception.ServiceError;
import com.fsm.livraria.estado.entities.Estado;
import com.fsm.livraria.estado.validation.EstadoUniqueName;
import com.fsm.livraria.pais.entities.Pais;
import com.fsm.livraria.pais.repositories.PaisRepository;
import com.fsm.livraria.pais.validation.PaisExist;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;
import java.util.UUID;

@Serdeable
public class EstatoCreatedRequest {

    @NotBlank(message = "O nome do estado não pode ser vazio")
    @EstadoUniqueName
    private String name;

    @NotBlank(message = "A UF do estado não pode ser vazia")
    @Size(max = 2, message = "A UF deve ter no máximo 2 caracteres")
    private String uf;

    @NotNull(message = "O ID do país não pode ser nulo")
    @PaisExist
    private String paisId;


    public EstatoCreatedRequest(String name, String uf, String paisId) {
        this.name = name;
        this.uf = uf;
        this.paisId = paisId;
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


    public String getPaisId() {
        return paisId;
    }

    public void setPaisId(String paisId) {
        this.paisId = paisId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EstatoCreatedRequest that = (EstatoCreatedRequest) o;
        return Objects.equals(name, that.name) && Objects.equals(uf, that.uf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, uf);
    }

    public Estado toEntity(@NotNull PaisRepository paisRepository) {

        if (paisRepository == null) {
            throw new IllegalArgumentException("O repositório de países não pode ser nulo");
        }

        Pais pais = paisRepository.findByUuid(UUID.fromString(paisId)).orElseThrow(() -> new ServiceError("País não encontrado com o ID fornecido"));

        return new Estado(name, uf, pais);

    }
}
