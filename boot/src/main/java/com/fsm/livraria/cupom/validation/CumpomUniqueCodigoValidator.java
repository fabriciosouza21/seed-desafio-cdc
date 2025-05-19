package com.fsm.livraria.cupom.validation;

import com.fsm.livraria.cupom.repositories.CupomRepository;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Singleton
public class CumpomUniqueCodigoValidator implements ConstraintValidator<CupomUniqueCodigo, String> {


    private final CupomRepository cupomRepository;

    public CumpomUniqueCodigoValidator(CupomRepository cupomRepository) {
        this.cupomRepository = cupomRepository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true; // deixa @NotBlank/@Email lidarem com isso
        }

        return !cupomRepository.existsByCodigo(value);
    }
}
