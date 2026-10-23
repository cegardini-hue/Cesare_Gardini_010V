package com.pedidos.pedido_ordenes;

import com.pedidos.pedido_ordenes.controller.PedidoController;
import com.pedidos.pedido_ordenes.model.Pedido;
import com.pedidos.pedido_ordenes.model.PedidoDetalle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:pedidos-test;DB_CLOSE_DELAY=-1",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.jpa.hibernate.ddl-auto=create-drop"
})
class PedidoOrdenesApplicationTests {

	@Autowired
	private PedidoController controller;

	@Test
	void contextLoads() {
	}

	@Test
	void persistsOrderAndItsItemsAndSupportsCrud() {
		Pedido pedido = new Pedido();
		pedido.setUsuarioId(1L);
		pedido.setTotal(new BigDecimal("19990"));
		PedidoDetalle item = new PedidoDetalle();
		item.setProductoId(1L);
		item.setCantidad(1);
		item.setPrecioUnitario(new BigDecimal("19990"));
		pedido.agregarItem(item);

		var creado = controller.crear(pedido).getBody();
		assertThat(creado).isNotNull();
		assertThat(creado.getId()).isNotNull();
		assertThat(creado.getItems()).hasSize(1);
		assertThat(controller.obtenerPorId(creado.getId()).getBody().getItems()).hasSize(1);

		Pedido cambios = new Pedido();
		cambios.setUsuarioId(1L);
		cambios.setTotal(new BigDecimal("29990"));
		cambios.setEstado("PENDIENTE");
		PedidoDetalle otroItem = new PedidoDetalle();
		otroItem.setProductoId(2L);
		otroItem.setCantidad(2);
		otroItem.setPrecioUnitario(new BigDecimal("14995"));
		cambios.agregarItem(otroItem);
		var actualizado = controller.actualizar(creado.getId(), cambios).getBody();
		assertThat(actualizado.getItems()).hasSize(1);
		assertThat(actualizado.getItems().get(0).getProductoId()).isEqualTo(2L);

		controller.eliminar(creado.getId());
		assertThat(controller.obtenerPorId(creado.getId()).getStatusCode().value()).isEqualTo(404);
	}
}
