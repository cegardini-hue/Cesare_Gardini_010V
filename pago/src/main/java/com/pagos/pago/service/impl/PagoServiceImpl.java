package com.pagos.pago.service.impl;

import com.pagos.pago.model.Pago;
import com.pagos.pago.repository.PagoRepository;
import com.pagos.pago.service.PagoService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;

    public PagoServiceImpl(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    @Override
    public Pago procesarPago(Pago pago) {
        pago.setEstado("COMPLETADO");
        return pagoRepository.save(pago);
    }

    @Override
    public Optional<Pago> obtenerPorPedidoId(Long pedidoId) {
        return pagoRepository.findByPedidoId(pedidoId);
    }
}