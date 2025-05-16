

CREATE TABLE carrinho(
    id BIGSERIAL PRIMARY KEY NOT NULL,
    uuid UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    total DECIMAL(10,2) NOT NULL
);


CREATE TABLE carrinho_item(
    id BIGSERIAL PRIMARY KEY NOT NULL,
    uuid UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    quantidade INT NOT NULL,
    carrinho_id BIGINT NOT NULL REFERENCES carrinho(id),
    livro_id BIGINT NOT NULL REFERENCES livro(id)
);