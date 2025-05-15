package com.fsm.livraria.autor.repositories;

import com.fsm.livraria.autor.entities.Autor;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface AutorRepository extends CrudRepository<Autor, Long> {
    boolean existsByEmail(String email);
    boolean existsByUuid(@NotNull UUID  nome);

    Optional<Autor> findByUuid(@NotNull UUID autorId);

    List<Autor> findAll(Pageable pageable);

}
