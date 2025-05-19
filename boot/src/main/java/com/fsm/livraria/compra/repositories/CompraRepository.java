package com.fsm.livraria.compra.repositories;

import com.fsm.livraria.compra.entities.Compra;
import io.micronaut.data.annotation.Join;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface CompraRepository extends CrudRepository<Compra, Long> {
    @Join("estado.pais")
    @Join("carrinho.itens.livro.autor")
    @Join("carrinho.itens.livro.categoria")
    Optional<Compra> findByUuid(UUID uuid);
}
