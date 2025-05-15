package com.fsm.livraria.estado.repositories;

import com.fsm.livraria.estado.entities.Estado;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface EstadoRepository extends CrudRepository<Estado, Long> {


    boolean existsByName(String value);
}
