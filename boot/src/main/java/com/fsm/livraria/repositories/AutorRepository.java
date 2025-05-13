package com.fsm.livraria.repositories;

import com.fsm.livraria.entities.Autor;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface AutorRepository extends CrudRepository<Autor, Long> {
    boolean existsByEmail(String email);
}
