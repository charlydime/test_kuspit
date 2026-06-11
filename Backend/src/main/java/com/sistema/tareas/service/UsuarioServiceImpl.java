package com.sistema.tareas.service;

import com.sistema.tareas.dto.UsuarioDTO;
import com.sistema.tareas.model.entity.Usuario;
import com.sistema.tareas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO) {
        log.debug("Creando usuario con username: {}", usuarioDTO.getUsername());

        Usuario usuario = Usuario.builder()
                .username(usuarioDTO.getUsername())
                .password(passwordEncoder.encode(usuarioDTO.getPassword()))
                .email(usuarioDTO.getEmail())
                .build();

        usuario = usuarioRepository.save(usuario);
        log.info("Usuario creado con id: {}", usuario.getId());
        return toDTO(usuario);
    }

    @Override
    public UsuarioDTO obtenerPorId(Long id) {
        log.debug("Buscando usuario por id: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        return toDTO(usuario);
    }

    @Override
    public UsuarioDTO obtenerPorUsername(String username) {
        log.debug("Buscando usuario por username: {}", username);
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));
        return toDTO(usuario);
    }

    @Override
    public List<UsuarioDTO> listarTodos() {
        log.debug("Listando todos los usuarios");
        return usuarioRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        log.debug("Eliminando usuario con id: {}", id);
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
        log.info("Usuario eliminado con id: {}", id);
    }

    @Override
    public Usuario buscarOCrear(Usuario usuario) {
        log.debug("Buscando o creando usuario con username: {}", usuario.getUsername());
        return usuarioRepository.findByUsername(usuario.getUsername())
                .orElseGet(() -> usuarioRepository.save(usuario));
    }

    private UsuarioDTO toDTO(Usuario usuario) {
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .build();
    }
}
