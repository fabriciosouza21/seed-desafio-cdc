package com.fsm.livraria.compra.repositories;

import com.fsm.livraria.compra.entities.CompraPedido;
import com.fsm.livraria.compra.entities.CompraPedidoId;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface CompraPedidoRepository extends CrudRepository<CompraPedido, CompraPedidoId> {
}
