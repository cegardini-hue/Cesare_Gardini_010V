package com.tienda.gateway;

import com.tienda.gateway.security.JwtAuthenticationFilter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GatewayApplicationTests {

	@Autowired
	private RouteLocator routeLocator;

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	private static final String JWT_SECRET = "mi_clave_secreta_super_segura_para_jwt_32_bytes";

	@Test
	void routesRequestsToEachMicroservice() {
		Map<String, URI> destinations = routeLocator.getRoutes()
				.collectMap(Route::getId, Route::getUri)
				.block();

		assertThat(destinations)
				.containsEntry("autentificador", URI.create("http://localhost:8081"))
				.containsEntry("producto-catalogo", URI.create("http://localhost:8082"))
				.containsEntry("pedido-ordenes", URI.create("http://localhost:8083"))
				.containsEntry("pago", URI.create("http://localhost:8084"));
	}

	@Test
	void allowsLoginWithoutToken() {
		var exchange = MockServerWebExchange.from(
				MockServerHttpRequest.post("/api/auth/login").build());
		AtomicBoolean chainCalled = new AtomicBoolean();

		jwtAuthenticationFilter.filter(exchange, recordingChain(chainCalled)).block();

		assertThat(chainCalled).isTrue();
	}

	@Test
	void rejectsProtectedRequestsWithoutToken() {
		var exchange = MockServerWebExchange.from(
				MockServerHttpRequest.get("/api/productos").build());
		AtomicBoolean chainCalled = new AtomicBoolean();

		jwtAuthenticationFilter.filter(exchange, recordingChain(chainCalled)).block();

		assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
		assertThat(chainCalled).isFalse();
	}

	@Test
	void rejectsExpiredTokens() {
		String expiredToken = Jwts.builder()
				.setSubject("usuario@ejemplo.com")
				.setExpiration(new Date(System.currentTimeMillis() - 1000))
				.signWith(Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8)),
						SignatureAlgorithm.HS256)
				.compact();
		var exchange = MockServerWebExchange.from(
				MockServerHttpRequest.get("/api/productos")
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + expiredToken)
						.build());
		AtomicBoolean chainCalled = new AtomicBoolean();

		jwtAuthenticationFilter.filter(exchange, recordingChain(chainCalled)).block();

		assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
		assertThat(chainCalled).isFalse();
	}

	@Test
	void rejectsTokensSignedWithAnotherSecret() {
		String invalidToken = Jwts.builder()
				.setSubject("usuario@ejemplo.com")
				.setExpiration(new Date(System.currentTimeMillis() + 60_000))
				.signWith(Keys.hmacShaKeyFor(
						"otra_clave_secreta_super_segura_para_jwt".getBytes(StandardCharsets.UTF_8)),
						SignatureAlgorithm.HS256)
				.compact();
		var exchange = MockServerWebExchange.from(
				MockServerHttpRequest.get("/api/productos")
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + invalidToken)
						.build());
		AtomicBoolean chainCalled = new AtomicBoolean();

		jwtAuthenticationFilter.filter(exchange, recordingChain(chainCalled)).block();

		assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
		assertThat(chainCalled).isFalse();
	}

	@Test
	void acceptsValidTokensForProtectedRequests() {
		String token = Jwts.builder()
				.setSubject("usuario@ejemplo.com")
				.setExpiration(new Date(System.currentTimeMillis() + 60_000))
				.signWith(Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8)),
						SignatureAlgorithm.HS256)
				.compact();
		var exchange = MockServerWebExchange.from(
				MockServerHttpRequest.get("/api/productos")
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
						.build());
		AtomicBoolean chainCalled = new AtomicBoolean();

		jwtAuthenticationFilter.filter(exchange, recordingChain(chainCalled)).block();

		assertThat(chainCalled).isTrue();
	}

	private GatewayFilterChain recordingChain(AtomicBoolean chainCalled) {
		return exchange -> {
			chainCalled.set(true);
			return Mono.empty();
		};
	}
}
