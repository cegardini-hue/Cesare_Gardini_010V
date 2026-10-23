package com.pagos.pago;

import com.pagos.pago.controller.PagoController;
import com.pagos.pago.model.Pago;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:pagos-test;DB_CLOSE_DELAY=-1",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.jpa.hibernate.ddl-auto=create-drop"
})
class PagoApplicationTests {

	@Autowired
	private PagoController controller;

	@Test
	void contextLoads() {
	}

	@Test
	void persistsPaymentCrudOperations() {
		Pago pago = new Pago();
		pago.setPedidoId(1L);
		pago.setMonto(new BigDecimal("19990"));
		pago.setMetodoPago("TRANSFERENCIA");

		var creado = controller.procesar(pago).getBody();
		assertThat(creado).isNotNull();
		assertThat(creado.getId()).isNotNull();
		assertThat(creado.getEstado()).isEqualTo("COMPLETADO");
		assertThat(controller.obtenerPorId(creado.getId()).getBody().getPedidoId()).isEqualTo(1L);

		Pago cambios = new Pago();
		cambios.setPedidoId(1L);
		cambios.setMonto(new BigDecimal("20990"));
		cambios.setMetodoPago("WEBPAY");
		cambios.setEstado("RECHAZADO");
		assertThat(controller.actualizar(creado.getId(), cambios).getBody().getEstado()).isEqualTo("RECHAZADO");
		assertThat(controller.listar().getBody()).hasSize(1);

		controller.eliminar(creado.getId());
		assertThat(controller.obtenerPorId(creado.getId()).getStatusCode().value()).isEqualTo(404);
	}
}
