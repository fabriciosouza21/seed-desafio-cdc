package com.fsm.livraria.compra.entities;

import io.micronaut.data.annotation.AutoPopulated;
import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.EmbeddedId;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.event.PrePersist;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@MappedEntity
public class CompraPedido  {

    @EmbeddedId
    private CompraPedidoId compraPedidoId;

    // @AutoPopulated gerará automaticamente um UUID ao persistir
    @AutoPopulated(updatable = false)
    private UUID uuid;

    // @DateCreated cuidará automaticamente da data de criação
    @DateCreated
    private LocalDateTime createdAt;

    public CompraPedido() {
    }

    public CompraPedido(CompraPedidoId compraPedidoId) {
        this.compraPedidoId = compraPedidoId;
    }

    public CompraPedidoId getCompraPedidoId() {
        return compraPedidoId;
    }

    public void setCompraPedidoId(CompraPedidoId compraPedidoId) {
        this.compraPedidoId = compraPedidoId;
    }

    @PrePersist
    public void prePersist() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }
        // Com @DateCreated, o Micronaut Data gerencia automaticamente o createdAt
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CompraPedido that = (CompraPedido) o;
        return Objects.equals(compraPedidoId, that.compraPedidoId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(compraPedidoId);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
