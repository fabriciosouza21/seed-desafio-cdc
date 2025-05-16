package com.fsm.livraria.carrinho.repositories;

import com.fsm.livraria.carrinho.entities.Carrinho;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface CarrinhoRepository extends CrudRepository<Carrinho, Long> {
}
