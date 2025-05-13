package com.fsm.livraria.validation;

import com.fsm.livraria.repositories.AutorRepository;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Singleton
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final AutorRepository autorRepository;

    public UniqueEmailValidator(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true; // deixa @NotBlank/@Email lidarem com isso
        }

        return !autorRepository.existsByEmail(value);
    }
}
