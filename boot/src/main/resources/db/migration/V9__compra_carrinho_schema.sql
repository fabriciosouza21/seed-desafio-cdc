ALTER TABLE compra add column carrinho_id bigint;

ALTER TABLE compra add constraint fk_compra_carrinho foreign key (carrinho_id) references carrinho(id);