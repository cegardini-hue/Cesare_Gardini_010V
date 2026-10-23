package com.pagos.pago.repository;

import com.pagos.pago.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    Optional<Pago> findByPedidoId(Long pedidoId);
    List<Pago> findAllByOrderByIdAsc();
}