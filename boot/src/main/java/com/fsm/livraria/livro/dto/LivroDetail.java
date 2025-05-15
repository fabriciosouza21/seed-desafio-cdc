package com.fsm.livraria.livro.dto;

import com.fsm.livraria.autor.dto.AutorDetail;
import com.fsm.livraria.categoria.dto.CategoriaDetail;
import com.fsm.livraria.livro.entities.Livro;
import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Serdeable
public class LivroDetail  {

    private UUID uuid;

    private String title;


    private String resume;

    private String summary;

    private BigDecimal price;

    private Integer numberPages;


    private String isbn;


    private LocalDateTime publicationDate;


    private AutorDetail author;

    private CategoriaDetail category;

    @Deprecated
    public LivroDetail() {
    }


    public LivroDetail(Livro livro) {
        this.uuid = livro.getUuid();
        this.title = livro.getTitulo();
        this.resume = livro.getResumo();
        this.summary = livro.getSumario();
        this.price = livro.getPreco();
        this.numberPages = livro.getNumeroPaginas();
        this.isbn = livro.getIsbn();
        this.publicationDate = livro.getDataPublicacao();
        this.author = new AutorDetail(livro.getAutor());
        this.category = new CategoriaDetail(livro.getCategoria());
    }

    public String getTitle() {
        return title;
    }

    public String getResume() {
        return resume;
    }

    public String getSummary() {
        return summary;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getNumberPages() {
        return numberPages;
    }

    public String getIsbn() {
        return isbn;
    }

    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }

    public UUID getUuid() {
        return uuid;
    }

    public AutorDetail getAuthor() {
        return author;
    }

    public CategoriaDetail getCategory() {
        return category;
    }

}
