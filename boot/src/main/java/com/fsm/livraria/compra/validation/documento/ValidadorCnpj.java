package com.fsm.livraria.compra.validation.documento;

public class ValidadorCnpj implements ValidadorDocumento{

    @Override
    public boolean validar(String documento) {
        // Remove caracteres especiais
        documento = documento.replaceAll("[^0-9]", "");

        // Verifica se possui 14 dígitos
        if (documento.length() != 14) {
            return false;
        }

        // Verifica se todos os dígitos são iguais
        boolean todosDigitosIguais = true;
        for (int i = 1; i < documento.length(); i++) {
            if (documento.charAt(i) != documento.charAt(0)) {
                todosDigitosIguais = false;
                break;
            }
        }

        if (todosDigitosIguais) {
            return false;
        }

        // Cálculo do primeiro dígito verificador
        int[] multiplicadores1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int soma = 0;

        for (int i = 0; i < 12; i++) {
            soma += Character.getNumericValue(documento.charAt(i)) * multiplicadores1[i];
        }

        int resto = soma % 11;
        int digitoVerificador1 = (resto < 2) ? 0 : 11 - resto;

        // Cálculo do segundo dígito verificador
        int[] multiplicadores2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        soma = 0;

        for (int i = 0; i < 13; i++) {
            soma += Character.getNumericValue(documento.charAt(i)) * multiplicadores2[i];
        }

        resto = soma % 11;
        int digitoVerificador2 = (resto < 2) ? 0 : 11 - resto;

        // Verifica se os dígitos verificadores estão corretos
        return (digitoVerificador1 == Character.getNumericValue(documento.charAt(12)) &&
                digitoVerificador2 == Character.getNumericValue(documento.charAt(13)));
    }
}
