package com.fsm.livraria.cupom.repositories;

import com.fsm.livraria.cupom.entities.Cupom;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface CupomRepository extends CrudRepository<Cupom, Long> {
    boolean existsByCodigo(String codigo);
}
