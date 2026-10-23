package com.autentificacion_usuario.autentificador.controller;

import com.autentificacion_usuario.autentificador.dto.AuthDTO;
import com.autentificacion_usuario.autentificador.model.Usuario;
import com.autentificacion_usuario.autentificador.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registro")
    public ResponseEntity<Usuario> registrar(@Valid @RequestBody Usuario usuario) {
        return new ResponseEntity<>(authService.registrar(usuario), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDTO.TokenResponse> login(@RequestBody AuthDTO.LoginRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(new AuthDTO.TokenResponse(token));
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable Long id) {
        return authService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/usuario/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody Usuario usuario) {
        return ResponseEntity.ok(authService.actualizar(id, usuario));
    }

    @DeleteMapping("/usuario/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        authService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}