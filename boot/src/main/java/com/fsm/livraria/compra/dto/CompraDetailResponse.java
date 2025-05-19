package com.fsm.livraria.compra.dto;

import com.fsm.livraria.compra.entities.Compra;
import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

@Serdeable
public class CompraDetailResponse {

    private String email;

    private String nome;


    private String sobrenome;

    private String documento;

    private String endereco;

    private String complemento;

    private String cidade;

    private String telefone;

    private String cep;

    private EstadoDetailResponse estado;

    private CompraPedidoDetailResponse compraPedido;

    private BigDecimal valorTotalDesconto;

    public CompraDetailResponse() {
    }

    public CompraDetailResponse(Compra c) {
        email = c.getEmail();
        nome = c.getNome();
        sobrenome = c.getSobrenome();
        documento = c.getDocumento();
        endereco = c.getEndereco();
        complemento = c.getComplemento();
        cidade = c.getCidade();
        telefone = c.getTelefone();
        cep = c.getCep();
        estado = new EstadoDetailResponse(c.getEstado());
        compraPedido = new CompraPedidoDetailResponse(c.getCarrinho());
        if(c.getCupomAplicado().getDesconto() == null){
            valorTotalDesconto = c.getCarrinho().getTotal();
        }else {
            valorTotalDesconto = c.getCarrinho().getTotal().subtract(BigDecimal.valueOf(c.getCupomAplicado().getDesconto()));
        }

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

    public EstadoDetailResponse getEstado() {
        return estado;
    }

    public void setEstado(EstadoDetailResponse estado) {
        this.estado = estado;
    }

    public CompraPedidoDetailResponse getCompraPedido() {
        return compraPedido;
    }

    public void setCompraPedido(CompraPedidoDetailResponse compraPedido) {
        this.compraPedido = compraPedido;
    }

    public BigDecimal getValorTotalDesconto() {
        return valorTotalDesconto;
    }

    public void setValorTotalDesconto(BigDecimal valorTotalDesconto) {
        this.valorTotalDesconto = valorTotalDesconto;
    }


}
