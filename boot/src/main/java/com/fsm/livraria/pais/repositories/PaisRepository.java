package com.fsm.livraria.pais.repositories;

import com.fsm.livraria.pais.entities.Pais;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface PaisRepository extends CrudRepository<Pais, Long> {
    boolean existsByName(String value);

    boolean existsByUuid(UUID uuid);

    default boolean existsByUuid(String value){
       return existsByUuid(UUID.fromString(value));
    }

    Optional<Pais> findByUuid(@NotNull UUID paisId);

    List<Pais> findAll(Pageable pageable);

    @Query(
            value = """
        SELECT pais_.*
        FROM pais pais_
        WHERE pais_.uuid != :uuid
        """,
            nativeQuery = true
    )
    List<Pais> findAllExcept(UUID uuid, Pageable pageable);
}
