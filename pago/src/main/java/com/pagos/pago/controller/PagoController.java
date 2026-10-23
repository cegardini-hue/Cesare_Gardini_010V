package com.pagos.pago.controller;

import com.pagos.pago.model.Pago;
import com.pagos.pago.service.PagoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping
    public ResponseEntity<List<Pago>> listar() {
        return ResponseEntity.ok(pagoService.listarPagos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> obtenerPorId(@PathVariable Long id) {
        return pagoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pago> procesar(@Valid @RequestBody Pago pago) {
        return new ResponseEntity<>(pagoService.procesarPago(pago), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pago> actualizar(@PathVariable Long id, @Valid @RequestBody Pago pago) {
        return ResponseEntity.ok(pagoService.actualizarPago(id, pago));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pagoService.eliminarPago(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<Pago> obtenerPorPedido(@PathVariable Long pedidoId) {
        return pagoService.obtenerPorPedidoId(pedidoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}