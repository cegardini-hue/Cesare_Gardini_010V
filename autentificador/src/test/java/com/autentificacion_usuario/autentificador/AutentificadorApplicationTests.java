package com.autentificacion_usuario.autentificador;

import com.autentificacion_usuario.autentificador.controller.AuthController;
import com.autentificacion_usuario.autentificador.dto.AuthDTO;
import com.autentificacion_usuario.autentificador.model.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:autentificador-test;DB_CLOSE_DELAY=-1",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.jpa.hibernate.ddl-auto=create-drop"
})
class AutentificadorApplicationTests {

	@Autowired
	private AuthController controller;

	@Test
	void contextLoads() {
	}

	@Test
	void persistsUserCrudAndDoesNotSerializePassword() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		Usuario usuario = new Usuario();
		usuario.setNombre("Ana");
		usuario.setEmail("ana@example.com");
		usuario.setPassword("secreto123");

		Usuario creado = controller.registrar(usuario).getBody();
		assertThat(creado).isNotNull();
		assertThat(creado.getId()).isNotNull();
		assertThat(objectMapper.writeValueAsString(creado)).doesNotContain("secreto123");
		assertThat(controller.login(new AuthDTO.LoginRequest("ana@example.com", "secreto123"))
				.getBody().getToken()).isNotBlank();

		Usuario cambios = new Usuario();
		cambios.setNombre("Ana actualizada");
		cambios.setEmail("ana@example.com");
		cambios.setPassword("secreto456");
		assertThat(controller.actualizarUsuario(creado.getId(), cambios).getBody().getNombre())
				.isEqualTo("Ana actualizada");

		controller.eliminarUsuario(creado.getId());
		assertThat(controller.obtenerUsuario(creado.getId()).getStatusCode().value()).isEqualTo(404);
	}
}
