package com.fsm.livraria.compra.validation.documento;

import com.fsm.livraria.compra.dto.CompraCreateResquest;
import com.fsm.livraria.estado.repositories.EstadoRepository;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.UUID;

@Singleton
public class EstadoPaisExistValidator implements ConstraintValidator<EstadoPaisExist, CompraCreateResquest> {


    private final EstadoRepository estadoRepository;

    public EstadoPaisExistValidator(EstadoRepository estadoRepository) {
        this.estadoRepository = estadoRepository;
    }

    @Override
    public boolean isValid(CompraCreateResquest compraCreateResquest, ConstraintValidatorContext context) {
        try {

            // Implemente sua lógica de validação aqui
            return validarCampos(compraCreateResquest);

        } catch (Exception e) {
            // Tratamento de erro
            return false;
        }
    }

    private boolean validarCampos(CompraCreateResquest compraCreateResquest) {
        return this.estadoRepository.existsByEstadoPais(UUID.fromString(compraCreateResquest.getPaisId()), UUID.fromString(compraCreateResquest.getEstadoId()));
    }

}
