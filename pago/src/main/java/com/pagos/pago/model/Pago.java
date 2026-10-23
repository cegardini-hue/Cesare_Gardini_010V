package com.pagos.pago.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "pagos", uniqueConstraints = @UniqueConstraint(columnNames = "pedido_id"))
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
    @NotNull
    @Positive
    private Long pedidoId;

    @Column(nullable = false)
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal monto;

    @Column(nullable = false)
    @NotBlank
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