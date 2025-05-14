package com.fsm.livraria.livro.repositories;

import com.fsm.livraria.livro.entities.Livro;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface LivroRepository extends CrudRepository<Livro, Long > {

    boolean existsByTitulo(String value);

    boolean existsByIsbn(String value);
}
