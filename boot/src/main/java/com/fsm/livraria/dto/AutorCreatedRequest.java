package com.fsm.livraria.dto;

import com.fsm.livraria.entities.Autor;
import com.fsm.livraria.validation.UniqueEmail;
import io.micronaut.core.annotation.ReflectiveAccess;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Serdeable
@ReflectiveAccess
public record AutorCreatedRequest(
        @NotBlank(message = "O nome é obrigatório") String name,
        @NotBlank(message = "O email é obrigatório") @Email(message = "O email está em formato inválido") @UniqueEmail String email,
        @NotBlank(message = "A descrição é obrigatória") @Size(max = 400, message = "O tamanho maximo da descrição é 400 caracteres") String description
) {

    public Autor toEntity() {
        return new Autor(name, email, description);
    }
}
