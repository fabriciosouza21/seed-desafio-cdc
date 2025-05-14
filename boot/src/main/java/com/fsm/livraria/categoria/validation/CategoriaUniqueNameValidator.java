package com.fsm.livraria.categoria.validation;

import com.fsm.livraria.categoria.repositories.CategoriaRepository;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Singleton
public class CategoriaUniqueNameValidator implements ConstraintValidator<CategoriaUniqueName, String> {

   private final CategoriaRepository categoriaRepository;

    public CategoriaUniqueNameValidator(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }

        return !categoriaRepository.existsByNome(value);
    }
}
