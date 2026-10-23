package com.productos.producto_catalogo.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    @NotBlank
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @Column(nullable = false)
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal precio;

    @Column(nullable = false)
    @NotNull
    @PositiveOrZero
    private Integer stock;
}