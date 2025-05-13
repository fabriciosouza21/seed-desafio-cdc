package com.fsm.livraria.entities;

import com.fsm.base.model.BaseDomain;
import com.fsm.exceptions.exception.ServiceError;
import io.micronaut.data.annotation.MappedEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@MappedEntity
public class Autor extends BaseDomain {

    @NotBlank
    private String nome;

    @NotBlank
    private String email;

    @NotBlank
    private String descricao;

    @Deprecated
    public Autor() {
    }

    public Autor(@NotBlank String nome, @NotBlank @Email String email, @NotBlank @Size(max = 400) String descricao) {
        if(nome == null || nome.isBlank()) {
            throw new ServiceError("Nome não pode ser nulo ou vazio");
        }
        if(email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email não pode ser nulo ou vazio");
        }
        if(descricao == null || descricao.isBlank() || descricao.length() > 400) {
            throw new IllegalArgumentException("Descrição não pode ser nula, vazia ou maior que 400 caracteres");
        }
        this.nome = nome;
        this.email = email;
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getDescricao() {
        return descricao;
    }
}
