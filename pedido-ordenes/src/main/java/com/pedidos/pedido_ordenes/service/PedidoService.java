package com.pedidos.pedido_ordenes.service;

import com.pedidos.pedido_ordenes.model.Pedido;
import java.util.List;
import java.util.Optional;

public interface PedidoService {
    List<Pedido> listarPedidos();
    Optional<Pedido> obtenerPorId(Long id);
    List<Pedido> obtenerPorUsuario(Long usuarioId);
    Pedido crearPedido(Pedido pedido);
}