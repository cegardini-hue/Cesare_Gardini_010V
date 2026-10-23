package com.pagos.pago.service;

import com.pagos.pago.model.Pago;
import java.util.Optional;

public interface PagoService {
    Pago procesarPago(Pago pago);
    Optional<Pago> obtenerPorPedidoId(Long pedidoId);
}