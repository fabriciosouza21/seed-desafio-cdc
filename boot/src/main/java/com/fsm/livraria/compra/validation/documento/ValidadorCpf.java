package com.fsm.livraria.compra.validation.documento;

public class ValidadorCpf implements ValidadorDocumento {


    /**
     * Método que valida um CPF
     *
     * @param cpf O CPF a ser validado (pode conter pontos e traço ou apenas números)
     * @return true se o CPF for válido, false caso contrário
     */

    @Override
    public boolean validar(String cpf) {
        // Remove caracteres especiais
        cpf = cpf.replaceAll("[^0-9]", "");

        // Verifica se possui 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }

        // Verifica se todos os dígitos são iguais
        boolean todosDigitosIguais = true;
        for (int i = 1; i < cpf.length(); i++) {
            if (cpf.charAt(i) != cpf.charAt(0)) {
                todosDigitosIguais = false;
                break;
            }
        }

        if (todosDigitosIguais) {
            return false;
        }

        // Calcula o primeiro dígito verificador
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        int resto = (soma * 10) % 11;
        int digitoVerificador1 = (resto == 10) ? 0 : resto;

        // Calcula o segundo dígito verificador
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        resto = (soma * 10) % 11;
        int digitoVerificador2 = (resto == 10) ? 0 : resto;

        // Verifica se os dígitos verificadores estão corretos
        return (digitoVerificador1 == Character.getNumericValue(cpf.charAt(9)) &&
                digitoVerificador2 == Character.getNumericValue(cpf.charAt(10)));
    }
}
