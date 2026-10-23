package com.autentificacion_usuario.autentificador.service.impl;

import com.autentificacion_usuario.autentificador.config.JwtUtil;
import com.autentificacion_usuario.autentificador.dto.AuthDTO;
import com.autentificacion_usuario.autentificador.model.Usuario;
import com.autentificacion_usuario.autentificador.repository.UsuarioRepository;
import com.autentificacion_usuario.autentificador.service.AuthService;
import org.springframework.stereotype.Service;

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
            throw new RuntimeException("El email ya está registrado");
        }
        if (usuario.getRol() == null) {
            usuario.setRol("ROLE_USER");
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    public String login(AuthDTO.LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Nota: En producción debes comparar la contraseña encriptada (BCryptPasswordEncoder)
        if (!usuario.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return jwtUtil.generarToken(usuario.getEmail(), usuario.getRol());
    }

    @Override
    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }
}