package com.fsm.livraria.estado.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Documented
@Constraint(validatedBy = EstadoUniqueNomeValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EstadoUniqueName {
    String message() default "O nome do estado já existe";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}


