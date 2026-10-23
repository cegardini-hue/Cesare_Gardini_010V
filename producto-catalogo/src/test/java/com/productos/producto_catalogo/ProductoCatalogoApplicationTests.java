package com.productos.producto_catalogo;

import com.productos.producto_catalogo.controller.ProductoController;
import com.productos.producto_catalogo.model.Producto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:productos-test;DB_CLOSE_DELAY=-1",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.jpa.hibernate.ddl-auto=create-drop"
})
class ProductoCatalogoApplicationTests {

	@Autowired
	private ProductoController controller;

	@Test
	void contextLoads() {
	}

	@Test
	void persistsProductCrudOperations() {
		Producto nuevo = new Producto();
		nuevo.setNombre("Teclado");
		nuevo.setPrecio(new BigDecimal("19990"));
		nuevo.setStock(10);

		var creado = controller.crear(nuevo).getBody();
		assertThat(creado).isNotNull();
		assertThat(creado.getId()).isNotNull();
		assertThat(controller.obtenerPorId(creado.getId()).getBody().getNombre()).isEqualTo("Teclado");

		Producto cambios = new Producto();
		cambios.setNombre("Teclado actualizado");
		cambios.setPrecio(new BigDecimal("20990"));
		cambios.setStock(8);
		assertThat(controller.actualizar(creado.getId(), cambios).getBody().getNombre())
				.isEqualTo("Teclado actualizado");
		assertThat(controller.obtenerTodos().getBody()).hasSize(1);

		controller.eliminar(creado.getId());
		assertThat(controller.obtenerPorId(creado.getId()).getStatusCode().value()).isEqualTo(404);
	}
}
