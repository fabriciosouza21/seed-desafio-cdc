package com.fsm.livraria.compra.dto;

import com.fsm.base.validation.AtributosBasicos;
import com.fsm.base.validation.RelacionamentoCampos;
import com.fsm.exceptions.exception.ServiceError;
import com.fsm.livraria.carrinho.entities.Carrinho;
import com.fsm.livraria.carrinho.repositories.CarrinhoRepository;
import com.fsm.livraria.compra.entities.Compra;
import com.fsm.livraria.compra.validation.documento.CpfOrCnpj;
import com.fsm.livraria.compra.validation.documento.EstadoPaisExist;
import com.fsm.livraria.cupom.entities.Cupom;
import com.fsm.livraria.cupom.repositories.CupomRepository;
import com.fsm.livraria.estado.entities.Estado;
import com.fsm.livraria.estado.repositories.EstadoRepository;
import com.fsm.livraria.estado.validation.EstadoExist;
import com.fsm.livraria.pais.entities.Pais;
import com.fsm.livraria.pais.repositories.PaisRepository;
import com.fsm.livraria.pais.validation.PaisExist;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

@Serdeable
@EstadoPaisExist(
        message = "O estado enviado não pertence ao país informado",
        groups = RelacionamentoCampos.class
)
@GroupSequence({
        CompraCreateResquest.class, // Necessário para definir o grupo padrão
        AtributosBasicos.class,     // Primeiro executará as validações básicas
        RelacionamentoCampos.class  // Depois as validações de relacionamento
})
public class CompraCreateResquest {

    @Email(message = "O email deve ser válido", groups = AtributosBasicos.class)
    @NotBlank(message = "O email não pode ser vazio", groups = AtributosBasicos.class)
    private String email;

    @NotBlank(message = "O nome não pode ser vazio", groups = AtributosBasicos.class)
    private String nome;

    @NotBlank(message = "O sobrenome não pode ser vazio", groups = AtributosBasicos.class)
    private String sobrenome;

    @NotBlank(message = "O documento não pode ser vazio", groups = AtributosBasicos.class)
    @CpfOrCnpj(groups = AtributosBasicos.class)
    private String documento;

    @NotBlank(message = "O endereço não pode ser vazio", groups = AtributosBasicos.class)
    private String endereco;

    @NotBlank(message = "O complemento não pode ser vazio", groups = AtributosBasicos.class)
    private String complemento;

    @NotBlank(message = "A cidade não pode ser vazia", groups = AtributosBasicos.class)
    private String cidade;

    @NotBlank(message = "O telefone não pode ser vazio", groups = AtributosBasicos.class)
    private String telefone;

    @NotBlank(message = "O cep não pode ser vazio", groups = AtributosBasicos.class)
    private String cep;

    @NotBlank(message = "O estado não pode ser vazio", groups = AtributosBasicos.class)
    @EstadoExist(groups = AtributosBasicos.class)
    private String estadoId;

    @NotBlank(message = "O país não pode ser vazio", groups = AtributosBasicos.class)
    @PaisExist(groups = AtributosBasicos.class)
    private String paisId;

    @NotBlank(message = "O carrinho não pode ser vazio", groups = AtributosBasicos.class)
    private String carrinhoId;

    private String cupomCodigo;



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

    public String getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(String estadoId) {
        this.estadoId = estadoId;
    }

    public String getPaisId() {
        return paisId;
    }

    public void setPaisId(String paisId) {
        this.paisId = paisId;
    }

    public Compra toEntity(EstadoRepository estadoRepository, PaisRepository paisRepository, CupomRepository cupomRepository) {

        Estado estado = estadoRepository.findByUuid(UUID.fromString(estadoId)).orElseThrow(() -> new ServiceError("Estado não encontrado com o ID fornecido"));

        Pais pais = paisRepository.findByUuid(UUID.fromString(paisId)).orElseThrow(() -> new ServiceError("País não encontrado com o ID fornecido"));


        Compra compra = new Compra(
                this.email,
                this.nome,
                this.sobrenome,
                this.documento,
                this.endereco,
                this.complemento,
                this.cidade,
                this.telefone,
                this.cep,
                estado,
                pais

        );

        if(cupomCodigo != null && !cupomCodigo.isBlank()){
            validarCupom(cupomRepository, compra);
        }

        return compra;
    }

    private void validarCupom(CupomRepository cupomRepository, Compra compra) {

        Cupom cupom  = cupomRepository.findByCodigo(cupomCodigo)
                .orElseThrow(() -> new ServiceError("Cupom não encontrado "));
        if(!cupom.hasValido()){
            throw new ServiceError("O cupom está fora da validade");
        }

        compra.AtribuirCupom(cupom);
    }

    public String getCarrinhoId() {
        return carrinhoId;
    }

    public void setCarrinhoId(String carrinhoId) {
        this.carrinhoId = carrinhoId;
    }

    public void atribuirCarrinho(CarrinhoRepository carrinhoRepository, Compra compra) {
        Carrinho carrinho = carrinhoRepository.findByUuid(UUID.fromString(carrinhoId))
                .orElseThrow(() -> new ServiceError("Carrinho não encontrado com o ID fornecido"));
        compra.setCarrinho(carrinho);
    }

    public void setCupomCodigo(String cupomCodigo) {
        this.cupomCodigo = cupomCodigo;
    }

    public String getCupomCodigo() {
        return cupomCodigo;
    }
}
