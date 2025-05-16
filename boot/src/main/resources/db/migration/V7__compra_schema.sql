CREATE TABLE compra(
    id BIGSERIAL PRIMARY KEY NOT NULL,
    uuid UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    email VARCHAR(255) NOT NULL,
    nome VARCHAR(255) NOT NULL,
    sobrenome VARCHAR(255) NOT NULL,
    documento VARCHAR(14) NOT NULL,
    cidade VARCHAR(255) NOT NULL,
    endereco VARCHAR(255) NOT NULL,
    complemento VARCHAR(255) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    cep VARCHAR(10) NOT NULL,
    pais_id BIGINT NOT NULL REFERENCES pais(id),
    estado_id BIGINT NOT NULL REFERENCES estado(id)
);
