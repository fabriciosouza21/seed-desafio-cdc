package com.fsm.livraria.pais.validation;

import com.fsm.livraria.pais.repositories.PaisRepository;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Singleton
public class PaisExistValidator implements ConstraintValidator<PaisExist, String> {

    private final PaisRepository paisRepository;

    public PaisExistValidator(PaisRepository paisRepository) {
        this.paisRepository = paisRepository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true; // deixa @NotBlank/@Email lidarem com isso
        }

        return paisRepository.existsByUuid(value);
    }
}
