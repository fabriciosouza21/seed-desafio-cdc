package com.fsm.livraria.livro.repositories;

import com.fsm.exceptions.exception.NotFoundError;
import com.fsm.livraria.livro.dto.LivroList;
import com.fsm.livraria.livro.entities.Livro;
import io.micronaut.data.annotation.Join;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface LivroRepository extends CrudRepository<Livro, Long > {

    boolean existsByTitulo(String value);

    boolean existsByIsbn(String value);

    @Query(
            value = """
        SELECT livro_.titulo  title,livro_.uuid  uuid
        FROM livro livro_
        """,
            countQuery = """
        SELECT COUNT(*)
        FROM livro 
        """
    )
    Page<LivroList> findAllLivros(Pageable pageable);

    @Join(value = "autor")
    @Join(value = "categoria")
    Optional<Livro> findByUuid(UUID uuid);


    default Livro findByUuidOrElseThrow(String uuid) {
        return findByUuid(UUID.fromString(uuid)).orElseThrow(
                () -> new NotFoundError("Livro não encontrado com o uuid: " + uuid)
        );
    }

}
