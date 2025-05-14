package com.fsm.livraria.livro.validation;

import com.fsm.livraria.autor.repositories.AutorRepository;
import com.fsm.livraria.livro.repositories.LivroRepository;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Singleton
public class LivroUniqueTituloValidator implements ConstraintValidator<LivroUniqueTitulo, String> {

    private final LivroRepository livroRepository;

    public LivroUniqueTituloValidator(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true; // deixa @NotBlank/@Email lidarem com isso
        }

        return !livroRepository.existsByTitulo(value);
    }
}
