package com.fsm.livraria.livro.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fsm.exceptions.exception.ServiceError;
import com.fsm.livraria.autor.entities.Autor;
import com.fsm.livraria.autor.repositories.AutorRepository;
import com.fsm.livraria.categoria.entities.Categoria;
import com.fsm.livraria.categoria.repositories.CategoriaRepository;
import com.fsm.livraria.livro.entities.Livro;
import com.fsm.livraria.livro.validation.LivroUniqueIsbn;
import com.fsm.livraria.livro.validation.LivroUniqueTitulo;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Serdeable
public class LivroCreatedRequest {

    @NotBlank(message = "O título é obrigatório")
    @LivroUniqueTitulo
    private String title;

    @NotBlank(message = "O resumo é obrigatório")
    @Size(max = 500, message = "O resumo deve ter no máximo 400 caracteres")
    private String resume;

    private String summary;

    @NotBlank(message = "O ISBN é obrigatório")
    @LivroUniqueIsbn
    private String isbn;

    @NotNull(message = "O número de páginas é obrigatório")
    @Min(value = 100, message = "O número mínimo de páginas é 100")
    private Integer numberPages;

    @Future(message = "A data de publicação deve ser no futuro")
    @NotNull(message = "A data de publicação é obrigatória")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime publicationDate;

    @NotNull(message = "O preço é obrigatório")
    @Min(value = 20, message = "O preço mínimo é 20")
    private BigDecimal price;

    @NotNull(message = "O autor é obrigatório")
    private String autorId;

    @NotNull(message = "A categoria é obrigatória")
    private String categoryId;

    // Construtor padrão necessário para deserialização JSON
    public LivroCreatedRequest() {
    }

    // Construtor completo
    public LivroCreatedRequest(
            String title,
            String resume,
            String summary,
            String isbn,
            Integer numberPages,
            LocalDateTime publicationDate,
            BigDecimal price,
            String autorId,
            String categoryId) {
        this.title = title;
        this.resume = resume;
        this.summary = summary;
        this.isbn = isbn;
        this.numberPages = numberPages;
        this.publicationDate = publicationDate;
        this.price = price;
        this.autorId = autorId;
        this.categoryId = categoryId;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getResume() {
        return resume;
    }

    public String getSummary() {
        return summary;
    }

    public String getIsbn() {
        return isbn;
    }

    public Integer getNumberPages() {
        return numberPages;
    }

    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getAutorId() {
        return autorId;
    }

    public String getCategoryId() {
        return categoryId;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setNumberPages(Integer numberPages) {
        this.numberPages = numberPages;
    }

    public void setPublicationDate(LocalDateTime publicationDate) {
        this.publicationDate = publicationDate;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setAutorId(String autorId) {
        this.autorId = autorId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    // Método toEntity
    public Livro toEntity(AutorRepository autorRepository, CategoriaRepository categoriaRepository) {
        Autor autor = autorRepository.findByUuid(UUID.fromString(this.autorId)).orElseThrow(
                () -> new ServiceError("O autor não existe")
        );

        Categoria categoria = categoriaRepository.findByUuid(UUID.fromString(this.categoryId)).orElseThrow(
                () -> new ServiceError("A categoria não existe")
        );

        return new Livro(
                this.title,
                this.resume,
                this.summary,
                this.price,
                this.numberPages,
                isbn,
                publicationDate,
                autor,
                categoria
        );
    }

    // Equals e HashCode para comparações em testes
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LivroCreatedRequest that = (LivroCreatedRequest) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(isbn, that.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, isbn);
    }

    @Override
    public String toString() {
        return "LivroCreatedRequest{" +
                "title='" + title + '\'' +
                ", resume='" + resume + '\'' +
                ", summary='" + summary + '\'' +
                ", isbn='" + isbn + '\'' +
                ", numberPages=" + numberPages +
                ", publicationDate=" + publicationDate +
                ", price=" + price +
                ", autorId='" + autorId + '\'' +
                ", categoryId='" + categoryId + '\'' +
                '}';
    }
}
