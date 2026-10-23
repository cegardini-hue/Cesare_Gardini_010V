package com.pedidos.pedido_ordenes.service.impl;

import com.pedidos.pedido_ordenes.model.Pedido;
import com.pedidos.pedido_ordenes.repository.PedidoRepository;
import com.pedidos.pedido_ordenes.service.PedidoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoServiceImpl(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    @Override
    public Optional<Pedido> obtenerPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    @Override
    public List<Pedido> obtenerPorUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public Pedido crearPedido(Pedido pedido) {
        if (pedido.getEstado() == null) {
            pedido.setEstado("PENDIENTE");
        }
        return pedidoRepository.save(pedido);
    }
}