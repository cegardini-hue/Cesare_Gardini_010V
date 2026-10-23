package com.pagos.pago.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long pedidoId;

    @Column(nullable = false)
    private BigDecimal monto;

    @Column(nullable = false)
    private String metodoPago; // TC, TRANSFERENCIA, WEBPAY

    @Column(nullable = false)
    private String estado; // COMPLETADO, RECHAZADO

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;

    @PrePersist
    public void prePersist() {
        this.fechaPago = LocalDateTime.now();
    }
}