package com.fsm.livraria.categoria.repositories;

import com.fsm.livraria.categoria.entities.Categoria;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface CategoriaRepository extends CrudRepository<Categoria, Long> {
    boolean existsByNome(String value);

}
