CREATE TABLE livro(
    id BIGSERIAL PRIMARY KEY NOT NULL,
    uuid UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    titulo VARCHAR(255) NOT NULL UNIQUE,
    resumo VARCHAR(500),
    sumario Text,
    preco NUMERIC(10,2) NOT NULL,
    isbn VARCHAR(255) NOT NULL UNIQUE,
    numero_paginas INT NOT NULL,
    data_publicacao TIMESTAMP NOT NULL,
    autor_id BIGINT NOT NULL REFERENCES autor(id),
    categoria_id BIGINT NOT NULL REFERENCES categoria(id)
);
