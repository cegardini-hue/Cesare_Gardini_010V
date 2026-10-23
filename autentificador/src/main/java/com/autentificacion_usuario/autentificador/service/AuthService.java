package com.autentificacion_usuario.autentificador.service;

import com.autentificacion_usuario.autentificador.dto.AuthDTO;
import com.autentificacion_usuario.autentificador.model.Usuario;

import java.util.Optional;

public interface AuthService {
    Usuario registrar(Usuario usuario);
    String login(AuthDTO.LoginRequest request);
    Optional<Usuario> obtenerPorId(Long id);
    Usuario actualizar(Long id, Usuario usuario);
    void eliminar(Long id);
}