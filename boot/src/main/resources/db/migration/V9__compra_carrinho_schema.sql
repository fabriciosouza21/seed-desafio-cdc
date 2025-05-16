CREATE TABLE compra_pedido(
    uuid UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    compra_pedido_id_compra_id BIGINT NOT NULL,
    compra_pedido_id_carrinho_id BIGINT NOT NULL,
    FOREIGN KEY (compra_pedido_id_compra_id) REFERENCES compra(id),
    FOREIGN KEY (compra_pedido_id_carrinho_id) REFERENCES carrinho(id),
    PRIMARY KEY (compra_pedido_id_compra_id, compra_pedido_id_carrinho_id)
);