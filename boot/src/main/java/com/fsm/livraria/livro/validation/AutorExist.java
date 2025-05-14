package com.fsm.livraria.livro.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Esta anotação é usada para validar se o UUID do autor existe no banco de dados.
 * */

@Documented
@Constraint(validatedBy = AutorExistValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutorExist {
    String message() default "autor não encontrado";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}


