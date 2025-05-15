package com.fsm.livraria.livro.entities;

import com.fsm.base.model.BaseDomain;
import com.fsm.exceptions.exception.ServiceError;
import com.fsm.livraria.autor.entities.Autor;
import com.fsm.livraria.categoria.entities.Categoria;
import com.fsm.livraria.livro.validation.LivroUniqueIsbn;
import com.fsm.livraria.livro.validation.LivroUniqueTitulo;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@MappedEntity
public class Livro extends BaseDomain {

    @NotBlank
    @LivroUniqueTitulo
    private String titulo;

    @NotBlank
    @Max(500)
    private String resumo;

    private String sumario;

    @Min(20)
    @NotNull
    private BigDecimal preco;

    @NotNull
    @Min(100)
    private Integer numeroPaginas;

    @NotBlank
    @LivroUniqueIsbn
    private String isbn;

    @Future
    private LocalDateTime dataPublicacao;


    @Relation(Relation.Kind.MANY_TO_ONE)
    private Autor autor;

    @Relation(Relation.Kind.MANY_TO_ONE)
    private Categoria categoria;

    @Deprecated
    public Livro() {
    }

    public Livro(@NotBlank String titulo, @NotBlank @Size(max = 500) String resumo, String sumario, @Min(20) @NotNull BigDecimal preco,
                 @NotNull @Min(100) Integer numeroPaginas, @NotBlank String isbn, @Future LocalDateTime dataPublicacao,
                 Autor autor, Categoria categoria) {

        if (titulo == null || titulo.isBlank()) {
            throw new ServiceError("Título não pode ser nulo ou vazio");
        }

        if (resumo == null || resumo.isBlank() || resumo.length() > 500) {
            throw new ServiceError("Resumo não pode ser nulo ou vazio");
        }

        if (isbn == null || isbn.isBlank()) {
            throw new ServiceError("ISBN não pode ser nulo ou vazio");
        }

        if (numeroPaginas == null || numeroPaginas < 100) {
            throw new ServiceError("Número de páginas não pode ser nulo ou menor que 100");
        }

        if (preco == null || preco.compareTo(BigDecimal.valueOf(20)) < 0) {
            throw new ServiceError("Preço não pode ser nulo ou menor que 20");
        }

        if (dataPublicacao == null || dataPublicacao.isBefore(LocalDateTime.now())) {
            throw new ServiceError("Data de publicação não pode ser nula ou no passado");
        }

        if (autor == null) {
            throw new ServiceError("Autor não pode ser nulo");
        }

        if (categoria == null) {
            throw new ServiceError("Categoria não pode ser nula");
        }


        this.titulo = titulo;
        this.resumo = resumo;
        this.sumario = sumario;
        this.preco = preco;
        this.numeroPaginas = numeroPaginas;
        this.isbn = isbn;
        this.dataPublicacao = dataPublicacao;
        this.autor = autor;
        this.categoria = categoria;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getResumo() {
        return resumo;
    }

    public String getSumario() {
        return sumario;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public Integer getNumeroPaginas() {
        return numeroPaginas;
    }

    public String getIsbn() {
        return isbn;
    }

    public LocalDateTime getDataPublicacao() {
        return dataPublicacao;
    }

    public Autor getAutor() {
        return autor;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public void setSumario(String sumario) {
        this.sumario = sumario;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public void setNumeroPaginas(Integer numeroPaginas) {
        this.numeroPaginas = numeroPaginas;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setDataPublicacao(LocalDateTime dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
