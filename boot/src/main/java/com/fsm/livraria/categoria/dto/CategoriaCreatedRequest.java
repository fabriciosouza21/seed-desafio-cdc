package com.fsm.livraria.categoria.dto;

import com.fsm.exceptions.exception.ServiceError;
import com.fsm.livraria.categoria.entities.Categoria;
import com.fsm.livraria.categoria.validation.CategoriaUniqueName;
import io.micronaut.core.annotation.ReflectiveAccess;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

@Serdeable
@ReflectiveAccess
public record CategoriaCreatedRequest (
       @NotBlank @CategoriaUniqueName String name
) {


    public Categoria toEntity() {

        if(name == null || name.isBlank()) {
            throw new ServiceError("Nome não pode ser nulo ou vazio");
        }

        return new Categoria(name);
    }
}
