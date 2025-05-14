package com.fsm.livraria.livro.validation;

import com.fsm.livraria.livro.repositories.LivroRepository;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Singleton
public class LivroUniqueIsbnValidator implements ConstraintValidator<LivroUniqueIsbn, String> {

    private final LivroRepository livroRepository;

    public LivroUniqueIsbnValidator(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true; // deixa @NotBlank/@Email lidarem com isso
        }

        return !livroRepository.existsByIsbn(value);
    }
}
