package com.fsm.livraria.compra.repositories;

import com.fsm.livraria.compra.entities.Compra;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface CompraRepository extends CrudRepository<Compra, Long> {
}
