package com.pedidos.pedido_ordenes.service.impl;

import com.pedidos.pedido_ordenes.model.Pedido;
import com.pedidos.pedido_ordenes.model.PedidoDetalle;
import com.pedidos.pedido_ordenes.repository.PedidoRepository;
import com.pedidos.pedido_ordenes.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        pedido.getItems().forEach(item -> item.setPedido(pedido));
        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido actualizarPedido(Long id, Pedido pedido) {
        Pedido existente = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));
        existente.setUsuarioId(pedido.getUsuarioId());
        existente.setTotal(pedido.getTotal());
        existente.setEstado(pedido.getEstado());
        existente.getItems().clear();
        for (PedidoDetalle item : pedido.getItems()) {
            PedidoDetalle nuevoItem = new PedidoDetalle();
            nuevoItem.setProductoId(item.getProductoId());
            nuevoItem.setCantidad(item.getCantidad());
            nuevoItem.setPrecioUnitario(item.getPrecioUnitario());
            existente.agregarItem(nuevoItem);
        }
        return pedidoRepository.save(existente);
    }

    @Override
    public void eliminarPedido(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado");
        }
        pedidoRepository.deleteById(id);
    }
}