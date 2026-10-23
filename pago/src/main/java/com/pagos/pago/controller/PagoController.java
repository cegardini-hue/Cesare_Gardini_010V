package com.pagos.pago.controller;

import com.pagos.pago.model.Pago;
import com.pagos.pago.service.PagoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @PostMapping
    public ResponseEntity<Pago> procesar(@RequestBody Pago pago) {
        return new ResponseEntity<>(pagoService.procesarPago(pago), HttpStatus.CREATED);
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<Pago> obtenerPorPedido(@PathVariable Long pedidoId) {
        return pagoService.obtenerPorPedidoId(pedidoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}