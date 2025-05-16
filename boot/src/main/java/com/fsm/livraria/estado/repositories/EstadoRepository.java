package com.fsm.livraria.estado.repositories;

import com.fsm.livraria.estado.entities.Estado;
import io.micronaut.data.annotation.Join;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface EstadoRepository extends CrudRepository<Estado, Long> {


    boolean existsByName(String value);

    boolean existsByUuid(UUID uuid);

    @Query(
            value = """
        SELECT EXISTS (
            SELECT 1
            FROM estado estado_
            JOIN pais pais_ ON estado_.pais_id = pais_.id
            WHERE estado_.uuid = :estadoId AND pais_.uuid = :paisId
        )
        """,
            nativeQuery = true
    )
    boolean existsByEstadoPais(UUID paisId, UUID estadoId);

    @Join(value = "pais")
    Optional<Estado> findByUuid(UUID estadoId);

    @Join(
            value = "pais"
    )
    List<Estado> findAll(Pageable pageable);
}
