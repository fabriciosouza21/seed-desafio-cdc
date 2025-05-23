package com.fsm.livraria.autor.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * Esta anotação é usada para validar se o e-mail de um autor é único no banco de dados.
 * */

@Documented
@Constraint(validatedBy = AutorUniqueEmailValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutorUniqueEmail {
    String message() default "E-mail já está em uso";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}


