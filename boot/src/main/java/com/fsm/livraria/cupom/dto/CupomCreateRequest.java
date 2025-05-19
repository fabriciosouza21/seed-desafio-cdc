package com.fsm.livraria.cupom.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fsm.exceptions.exception.ServiceError;
import com.fsm.livraria.cupom.entities.Cupom;
import com.fsm.livraria.cupom.validation.CupomUniqueCodigo;
import io.micronaut.core.annotation.ReflectiveAccess;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Serdeable
@ReflectiveAccess
public record CupomCreateRequest(

        @NotBlank(message = "O código é obrigatório")
        @CupomUniqueCodigo
        String codigo,

        @NotNull(message = "O tipo de desconto é obrigatório")
        @Min(value = 1, message = "O tipo de desconto deve ser maior que 0")
        Double desconto,

        @Future(message = "A validade deve ser uma data futura")
        @NotNull(message = "A validade é obrigatória")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime validade

) {
    public Cupom toEntity() {
         if(codigo == null || codigo.isBlank()) {
            throw new ServiceError("O código é obrigatório");
        }
        if(desconto == null || desconto <= 0) {
            throw new ServiceError("O desconto deve ser maior que 0");
        }
        if(validade == null || !validade.isAfter(LocalDateTime.now())) {
            throw new ServiceError("A validade deve ser uma data futura");
        }
        return new Cupom(codigo, desconto, validade);
    }
}
