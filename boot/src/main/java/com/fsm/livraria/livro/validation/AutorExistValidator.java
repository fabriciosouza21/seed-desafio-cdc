package com.fsm.livraria.livro.validation;

import com.fsm.livraria.autor.repositories.AutorRepository;
import com.fsm.livraria.livro.repositories.LivroRepository;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.UUID;

@Singleton
public class AutorExistValidator implements ConstraintValidator<AutorExist, String> {

    private final AutorRepository autorRepository;

    public AutorExistValidator(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true; // deixa @NotBlank/@Email lidarem com isso
        }

        return autorRepository.existsByUuid(UUID.fromString(value));
    }
}
