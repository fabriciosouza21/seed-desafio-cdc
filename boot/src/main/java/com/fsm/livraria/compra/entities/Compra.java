package com.fsm.livraria.compra.entities;

import com.fsm.base.model.BaseDomain;
import com.fsm.exceptions.exception.ServiceError;
import com.fsm.livraria.carrinho.entities.Carrinho;
import com.fsm.livraria.carrinho.repositories.CarrinhoRepository;
import com.fsm.livraria.compra.validation.documento.CpfOrCnpj;
import com.fsm.livraria.cupom.entities.Cupom;
import com.fsm.livraria.estado.entities.Estado;
import com.fsm.livraria.pais.entities.Pais;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@MappedEntity
public class Compra extends BaseDomain {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String nome;

    @NotBlank
    private String sobrenome;

    @NotBlank
    @CpfOrCnpj
    private String documento;

    @NotBlank
    private String endereco;

    @NotBlank
    private String complemento;

    @NotBlank
    private String cidade;

    @NotBlank
    private String telefone;

    @NotBlank
    private String cep;

    @NotNull
    @Relation(Relation.Kind.MANY_TO_ONE)
    private Estado estado;

    @NotNull
    @Relation(Relation.Kind.MANY_TO_ONE)
    private Pais pais;

    @Relation(Relation.Kind.EMBEDDED)
    private CupomAplicado cupomAplicado;

    @Relation(Relation.Kind.MANY_TO_ONE)
    private Carrinho carrinho;

    public Compra(String email, String nome, String sobrenome, String documento, String endereco, String complemento, String cidade, String telefone, String cep, Estado estado, Pais pais) {

        if (email == null || email.isBlank()) {
            throw new ServiceError("Email não pode ser nulo ou vazio");
        }

        if (nome == null || nome.isBlank()) {
            throw new ServiceError("Nome não pode ser nulo ou vazio");
        }

        if (sobrenome == null || sobrenome.isBlank()) {
            throw new ServiceError("Sobrenome não pode ser nulo ou vazio");
        }

        if (documento == null || documento.isBlank()) {
            throw new ServiceError("Documento não pode ser nulo ou vazio");
        }

        if (endereco == null || endereco.isBlank()) {
            throw new ServiceError("Endereço não pode ser nulo ou vazio");
        }

        if (complemento == null || complemento.isBlank()) {
            throw new ServiceError("Complemento não pode ser nulo ou vazio");
        }

        if (cidade == null || cidade.isBlank()) {
            throw new ServiceError("Cidade não pode ser nula ou vazia");
        }

        if (telefone == null || telefone.isBlank()) {
            throw new ServiceError("Telefone não pode ser nulo ou vazio");
        }

        if (cep == null || cep.isBlank()) {
            throw new ServiceError("CEP não pode ser nulo ou vazio");
        }

        if (estado == null) {
            throw new ServiceError("Estado não pode ser nulo ou vazio");
        }

        if (pais == null) {
            throw new ServiceError("País não pode ser nulo ou vazio");
        }


        this.email = email;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.documento = documento;
        this.endereco = endereco;
        this.complemento = complemento;
        this.cidade = cidade;
        this.telefone = telefone;
        this.cep = cep;
        setEstado(estado,pais);
        this.pais = pais;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado, Pais pais) {
        if(!estado.hasMeuPais(pais)) {
            throw  new ServiceError("O estado não pertence ao país");
        }

        this.estado = estado;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public void saveCompraCarrinho(CarrinhoRepository carrinhoRepository) {

    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public void AtribuirCupom(Cupom cupom) {
        this.cupomAplicado = new CupomAplicado(cupom);
    }

    public CupomAplicado getCupomAplicado() {
        return cupomAplicado;
    }

    public void setCupomAplicado(CupomAplicado cupomAplicado) {
        this.cupomAplicado = cupomAplicado;
    }

    public Carrinho getCarrinho() {
        return carrinho;
    }

    public void setCarrinho(Carrinho carrinho) {
        this.carrinho = carrinho;
    }
}
