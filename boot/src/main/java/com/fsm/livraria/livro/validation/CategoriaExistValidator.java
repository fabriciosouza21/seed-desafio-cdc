package com.fsm.livraria.livro.validation;

import com.fsm.livraria.autor.repositories.AutorRepository;
import com.fsm.livraria.categoria.repositories.CategoriaRepository;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.UUID;

@Singleton
public class CategoriaExistValidator implements ConstraintValidator<CategoriaExist, String> {

    private final CategoriaRepository categoriaRepository;

    public CategoriaExistValidator(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true; // deixa @NotBlank/@Email lidarem com isso
        }

        return categoriaRepository.existsByUuid(UUID.fromString(value));
    }
}
