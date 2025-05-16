package com.fsm.livraria.compra.validation.documento;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EstadoPaisExistValidator.class)
public @interface EstadoPaisExist {
    String message() default "O estado não existe no país";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}


