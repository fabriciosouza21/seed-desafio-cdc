package com.fsm.livraria.carrinho.repositories;

import com.fsm.livraria.carrinho.entities.Carrinho;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface CarrinhoRepository extends CrudRepository<Carrinho, Long> {
    Optional<Carrinho> findByUuid(UUID uuid);
}
