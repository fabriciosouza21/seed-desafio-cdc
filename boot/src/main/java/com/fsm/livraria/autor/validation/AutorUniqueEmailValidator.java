package com.fsm.livraria.autor.validation;

import com.fsm.livraria.autor.repositories.AutorRepository;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Singleton
public class AutorUniqueEmailValidator implements ConstraintValidator<AutorUniqueEmail, String> {

    private final AutorRepository autorRepository;

    public AutorUniqueEmailValidator(AutorRepository autorRepository) {
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
