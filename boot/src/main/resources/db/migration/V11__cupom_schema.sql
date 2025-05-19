ALTER TABLE compra add column cupom_aplicado_cupom_id BIGINT REFERENCES cupom(id);

ALTER TABLE compra add column cupom_aplicado_desconto DECIMAL(10,2);

ALTER TABLE compra add column cupom_aplicado_validade TIMESTAMP;