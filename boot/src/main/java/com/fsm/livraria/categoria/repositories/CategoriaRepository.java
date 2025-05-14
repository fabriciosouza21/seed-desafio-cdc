package com.fsm.livraria.categoria.repositories;

import com.fsm.livraria.categoria.entities.Categoria;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;
import java.util.UUID;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface CategoriaRepository extends CrudRepository<Categoria, Long> {
    boolean existsByNome(String value);

    boolean existsByUuid(UUID value);

    Optional<Categoria> findByUuid(@NotNull UUID categoryId);
}
