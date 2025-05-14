package com.fsm.livraria.livro.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Esta anotação é usada para validar se o e-mail de um autor é único no banco de dados.
 * */

@Documented
@Constraint(validatedBy = LivroUniqueIsbnValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LivroUniqueIsbn {
    String message() default "isbn já está em uso";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}


