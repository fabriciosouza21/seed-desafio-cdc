package com.fsm.livraria.estado.validation;

import com.fsm.livraria.estado.repositories.EstadoRepository;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.UUID;

@Singleton
public class EstadoExistValidator implements ConstraintValidator<EstadoUniqueName, String> {

    private final EstadoRepository estadoRepository;


    public EstadoExistValidator(EstadoRepository estadoRepository) {
        this.estadoRepository = estadoRepository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true; // deixa @NotBlank/@Email lidarem com isso
        }

        return !estadoRepository.existsByUuid(UUID.fromString(value));
    }
}
