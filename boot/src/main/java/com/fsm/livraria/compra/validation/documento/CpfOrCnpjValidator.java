package com.fsm.livraria.compra.validation.documento;

import jakarta.inject.Singleton;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Singleton
public class CpfOrCnpjValidator implements ConstraintValidator<CpfOrCnpj, String> {



    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }

        //validar primeiro o tamanho se for diferente de 11 ou 14

        if (value.length() != 11 && value.length() != 14) {
            return false;
        }

        //Obtem o validador correto

        ValidadorDocumento validador = value.length() == 11 ? new ValidadorCpf() : new ValidadorCnpj();

        //valida o documento

        return validador.validar(value);
    }
}
