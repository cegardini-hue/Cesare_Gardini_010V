package com.pedidos.pedido_ordenes.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    @Positive
    private Long usuarioId;

    @Column(nullable = false)
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal total;

    @Column(nullable = false)
    @NotBlank
    @Builder.Default
    private String estado = "PENDIENTE";

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Valid
    @Builder.Default
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PedidoDetalle> items = new ArrayList<>();

    public void agregarItem(PedidoDetalle item) {
        items.add(item);
        item.setPedido(this);
    }

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
    }
}