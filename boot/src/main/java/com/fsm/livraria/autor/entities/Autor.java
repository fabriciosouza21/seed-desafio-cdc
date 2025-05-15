package com.fsm.livraria.autor.entities;

import com.fsm.base.model.BaseDomain;
import com.fsm.exceptions.exception.ServiceError;
import com.fsm.livraria.autor.validation.AutorUniqueEmail;
import io.micronaut.data.annotation.MappedEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@MappedEntity
public class Autor extends BaseDomain {

    @NotBlank
    private String nome;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String descricao;

    @Deprecated
    public Autor() {
    }

    public Autor(@NotBlank String nome, @NotBlank @Email @AutorUniqueEmail String email, @NotBlank @Size(max = 400) String descricao) {
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

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
