package com.autentificacion_usuario.autentificador.service.impl;

import com.autentificacion_usuario.autentificador.config.JwtUtil;
import com.autentificacion_usuario.autentificador.dto.AuthDTO;
import com.autentificacion_usuario.autentificador.model.Usuario;
import com.autentificacion_usuario.autentificador.repository.UsuarioRepository;
import com.autentificacion_usuario.autentificador.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UsuarioRepository usuarioRepository, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Usuario registrar(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está registrado");
        }
        if (usuario.getRol() == null) {
            usuario.setRol("ROLE_USER");
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    public String login(AuthDTO.LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas"));

        // Nota: En producción debes comparar la contraseña encriptada (BCryptPasswordEncoder)
        if (!usuario.getPassword().equals(request.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas");
        }

        return jwtUtil.generarToken(usuario.getEmail(), usuario.getRol());
    }

    @Override
    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Usuario actualizar(Long id, Usuario cambios) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        if (usuarioRepository.existsByEmailAndIdNot(cambios.getEmail(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está registrado");
        }
        usuario.setNombre(cambios.getNombre());
        usuario.setEmail(cambios.getEmail());
        if (cambios.getPassword() != null && !cambios.getPassword().isBlank()) {
            usuario.setPassword(cambios.getPassword());
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }
}