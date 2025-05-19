package com.fsm.livraria.cupom.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Documented
@Constraint(validatedBy = CumpomUniqueCodigoValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CupomUniqueCodigo {
    String message() default "O código já está em uso";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}


