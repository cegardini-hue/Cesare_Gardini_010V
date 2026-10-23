package com.pagos.pago.service.impl;

import com.pagos.pago.model.Pago;
import com.pagos.pago.repository.PagoRepository;
import com.pagos.pago.service.PagoService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;

    public PagoServiceImpl(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    @Override
    public List<Pago> listarPagos() {
        return pagoRepository.findAllByOrderByIdAsc();
    }

    @Override
    public Optional<Pago> obtenerPorId(Long id) {
        return pagoRepository.findById(id);
    }

    @Override
    public Pago procesarPago(Pago pago) {
        pago.setEstado("COMPLETADO");
        return pagoRepository.save(pago);
    }

    @Override
    public Pago actualizarPago(Long id, Pago pago) {
        Pago existente = pagoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pago no encontrado"));
        existente.setPedidoId(pago.getPedidoId());
        existente.setMonto(pago.getMonto());
        existente.setMetodoPago(pago.getMetodoPago());
        if (pago.getEstado() != null) {
            existente.setEstado(pago.getEstado());
        }
        return pagoRepository.save(existente);
    }

    @Override
    public Optional<Pago> obtenerPorPedidoId(Long pedidoId) {
        return pagoRepository.findByPedidoId(pedidoId);
    }

    @Override
    public void eliminarPago(Long id) {
        if (!pagoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pago no encontrado");
        }
        pagoRepository.deleteById(id);
    }
}