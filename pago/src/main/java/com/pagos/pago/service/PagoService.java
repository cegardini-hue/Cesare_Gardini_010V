package com.pagos.pago.service;

import com.pagos.pago.model.Pago;
import java.util.List;
import java.util.Optional;

public interface PagoService {
    List<Pago> listarPagos();
    Optional<Pago> obtenerPorId(Long id);
    Pago procesarPago(Pago pago);
    Pago actualizarPago(Long id, Pago pago);
    Optional<Pago> obtenerPorPedidoId(Long pedidoId);
    void eliminarPago(Long id);
}