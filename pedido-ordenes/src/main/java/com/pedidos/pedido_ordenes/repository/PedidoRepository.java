package com.pedidos.pedido_ordenes.repository;

import com.pedidos.pedido_ordenes.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    @Override
    @EntityGraph(attributePaths = "items")
    List<Pedido> findAll();

    @Override
    @EntityGraph(attributePaths = "items")
    Optional<Pedido> findById(Long id);

    @EntityGraph(attributePaths = "items")
    List<Pedido> findByUsuarioId(Long usuarioId);
}